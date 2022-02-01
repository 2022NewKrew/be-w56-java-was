package framework.container;

import framework.util.annotation.Autowired;
import framework.util.annotation.Bean;
import framework.util.annotation.Component;
import framework.util.annotation.Primary;
import framework.util.exception.CannotFillContainerException;
import framework.util.exception.ClassNotFoundException;
import framework.util.exception.ComponentNotFoundException;
import framework.util.exception.ConstructorNotFoundException;
import framework.util.exception.MethodNotFoundException;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;

public class Container {
    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

    /** 각 Component의 생성자들 및 각 Bean 메소드 */
    private static final Map<String, Executable> EXECUTABLES = new HashMap<>();

    /** 생성자들과 메소드들을 실행하여 얻은 객체들을 저장할 Container */
    private static final Map<String, Object> CONTAINER = new HashMap<>();

    /** Component 클래스들 */
    private static Set<Class<?>> componentClasses;

    /** Bean 메소드들 */
    private static Set<Method> beanMethods;

    /**
     * Container에 들어갈 Component와 Bean을 모두 찾은 후 객체를 만들어 넣는 메소드
     * @param baseClass 현재 어플리케이션의 최상위 패키지를 알기 위한 클래스
     */
    public static void scanAndFill(Class<?> baseClass) {
        scanComponentsAndBeans(baseClass);
        fillContainer();
    }

    /**
     * Container에 들어갈 Component와 Bean을 모두 찾는 메소드
     * @param baseClass 현재 어플리케이션의 최상위 패키지를 알기 위한 클래스
     */
    private static void scanComponentsAndBeans(Class<?> baseClass) {
        Reflections reflections = new Reflections(baseClass.getPackageName(), Scanners.TypesAnnotated, Scanners.MethodsAnnotated);
        // @Component가 붙은 클래스에 대해 모두 확인
        componentClasses = reflections.getTypesAnnotatedWith(Component.class);

        // @Bean이 붙은 메소드에 대해 모두 확인
        beanMethods = reflections.getMethodsAnnotatedWith(Bean.class);

        // 각 Component의 생성자를 찾아서 저장
        saveConstructorsOfComponents();

        // 각 Bean의 반환 타입을 찾아서 저장
        saveBeansByReturnTypes();
    }

    /**
     * 각 Component의 생성자를 찾아서 저장해주는 메소드, 세 가지 경우에 맞게 확인
     */
    private static void saveConstructorsOfComponents() {
        for (Class<?> component : componentClasses) {
            // 정의한 생성자가 없는 경우 디폴트 생성자를 가져옴
            if (component.getDeclaredConstructors().length == 0) {
                putConstructor(component, component.getConstructors()[0]);
                continue;
            }

            // 정의한 생성자가 1개만 있는 경우 해당 생성자를 가져옴
            if (component.getDeclaredConstructors().length == 1) {
                putConstructor(component, component.getDeclaredConstructors()[0]);
                continue;
            }

            // 정의한 생성자가 여러 개인 경우 @Primary가 붙어있는 생성자를 가져옴, 없다면 예외 발생
            putConstructor(component,
                    Arrays.stream(component.getDeclaredConstructors())
                            .filter(c -> c.isAnnotationPresent(Primary.class))
                            .findFirst()
                            .orElseThrow(() -> new ComponentNotFoundException("정의한 생성자가 여러 개 있지만 @Primary를 사용한 생성자가 없어 구분이 안됩니다.")));
        }
    }

    /**
     * 현재 Component를 저장하고 현재 Component가 구현한 인터페이스에 대해서도 저장 (1 depth 까지만)해주는 메소드
     * @param component 현재 Component
     * @param constructor 현재 Component의 생성자
     */
    private static void putConstructor(Class<?> component, Constructor<?> constructor) {
        // 현재 Component에 대한 정보로 넣음
        EXECUTABLES.put(component.getName(), constructor);

        // 구현한 인터페이스에 관련해서도 넣음 (1 depth 까지만)
        Arrays.stream(component.getInterfaces()).forEach(i -> {
            EXECUTABLES.put(i.getName(), constructor);
        });
    }

    /**
     * 각 Bean의 반환 타입을 key로 지정하여 메소드를 저장해주는 메소드
     */
    private static void saveBeansByReturnTypes() {
        for (Method bean : beanMethods) {
            EXECUTABLES.put(bean.getReturnType().getName(), bean);
        }
    }

    /**
     * Container에 찾은 생성자와 메소드를 실행시켜 해당 객체를 넣는 메소드
     */
    private static void fillContainer() {
        // 실행시켜야할 생성자들, 메소드들을 모두 실행시킬 때까지 반복
        while (EXECUTABLES.size() != CONTAINER.size()) {
            // 현재 Container의 크기
            int currContainerSize = CONTAINER.size();

            // 현재 찾아놓은 모든 생성자들, 메소드들을 실행하여 Container에 객체 저장
            putAllInstancesInContainer();

            // 만약 Container에 아무것도 들어가지 않았다면 예외 발생
            if (currContainerSize == CONTAINER.size()) {
                throw new CannotFillContainerException();
            }
        }
    }

    /**
     * 현재 찾아놓은 모든 생성자들, 메소드들을 실행하여 Container에 객체 저장해주는 메소드
     */
    private static void putAllInstancesInContainer() {
        // 현재 찾아놓은 모든 생성자들, 메소드들을 확인
        for (Map.Entry<String, Executable> entry : EXECUTABLES.entrySet()) {
            // 현재 실행 후 받을 객체 타입
            String key = entry.getKey();

            // 이미 저장돼있다면 넘어감
            if (CONTAINER.containsKey(key)) {
                continue;
            }

            // 현재 생성자 또는 메소드 및 매개변수
            Executable executable = entry.getValue();
            Parameter[] parameters = executable.getParameters();

            // 해당 생성자 또는 메소드에 필요한 매개변수가 있는데 @Autowired가 붙어있지 않으면 의존성 주입을 못해주므로 예외 발생
            if (parameters.length != 0 && !executable.isAnnotationPresent(Autowired.class)) {
                throw new CannotFillContainerException();
            }

            // 현재 생성자 또는 메소드에 넘겨줄 인자들
            Object[] arguments = new Object[parameters.length];

            // 해당 인자들을 만들어 넣어주는데 문제가 발생한다면 다음으로 넘어감
            if (!fillArguments(parameters, arguments)) {
                continue;
            }

            // 해당 생성자 또는 메소드를 실행하여 얻은 객체를 Container에 저장
            putInstanceInContainer(key, executable, arguments);
        }
    }

    /**
     * 해당 매개변수에 맞는 인자들을 만들어 배열에 넣어주는 메소드, 해당 인자들은 Container에 이미 저장이 돼있어야 함
     * @param parameters 매개변수 배열
     * @param arguments 인자 배열
     * @return 모든 매개변수에 맞는 인자를 잘 만들어서 넣을 수 있는지에 대한 여부
     */
    private static boolean fillArguments(Parameter[] parameters, Object[] arguments) {
        for (int i = 0, size = parameters.length; i < size; i++) {
            String typeName = parameters[i].getType().getName();

            // 만약 현재 매개변수에 맞는 객체가 Container에 저장돼있지 않은 경우, 인자를 넣을 수 없음
            if (!CONTAINER.containsKey(typeName)) {
                return false;
            }

            arguments[i] = CONTAINER.get(typeName);
        }

        return true;
    }

    /**
     * 해당 생성자 또는 메소드를 실행하여 얻은 객체를 Container에 저장해주는 메소드
     * @param typeName 생성자 또는 메소드를 실행하여 얻는 객체의 타입
     * @param executable 생성자 또는 메소드
     * @param arguments 생성자 또는 메소드에 넘겨줄 인자들
     */
    private static void putInstanceInContainer(String typeName, Executable executable, Object[] arguments) {
        // 생성자의 경우
        if (executable instanceof Constructor) {
            try {
                // 해당 생성자에 인자를 넘겨 생성한 객체를 Container에 저장
                CONTAINER.put(typeName, ((Constructor<?>) executable).newInstance(arguments));
            } catch (Exception e) {
                throw new ConstructorNotFoundException();
            }

        }

        // 메소드의 경우
        if (executable instanceof Method) {
            // 해당 메소드를 갖는 클래스 이름
            String className = executable.getDeclaringClass().getName();

            // 해당 클래스가 Container에 없다면 실행할 수 없다고 판단하여 돌아감
            if (!CONTAINER.containsKey(className)) {
                return;
            }

            // 해당 클래스 객체를 Container에서 가져옴
            Object instance = CONTAINER.get(className);

            try {
                // 해당 클래스 객체와 인자를 넘겨 메소드를 실행한 뒤 반환된 객체를 Container에 저장
                CONTAINER.put(typeName, ((Method) executable).invoke(instance, arguments));
            } catch (Exception e) {
                throw new MethodNotFoundException(executable.getName() + ": 해당 메소드를 실행할 수 없습니다.");
            }
        }


        LOGGER.debug("Add instance in container: {}", typeName);
    }

    /**
     * Container에 저장된 객체의 클래스 이름을 통해 Container에 저장된 객체를 반환해주는 메소드
     * @param className Container에 저장된 객체의 클래스 이름
     * @return Container에서 찾은 객체
     */
    public static Object getInstanceFromContainer(String className) {
        // 해당 클래스 이름으로 저장된 객체를 찾을 수 없다면 예외 발생
        if (!CONTAINER.containsKey(className)) {
            throw new ClassNotFoundException();
        }

        return CONTAINER.get(className);
    }

    /**
     * container에 저장된 객체의 클래스를 통해 Container에 저장된 객체를 반환해주는 메소드
     * @param clazz Container에 저장된 객체의 클래스
     * @return Container에서 찾은 객체
     */
    public static Object getInstanceFromContainer(Class<?> clazz) {
        return getInstanceFromContainer(clazz.getName());
    }
