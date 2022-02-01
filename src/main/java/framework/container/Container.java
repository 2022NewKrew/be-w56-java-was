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
    private static final Map<String, Executable> EXECUTABLES = new HashMap<>();
    private static final Map<String, Object> CONTAINER = new HashMap<>();

    private static Set<Class<?>> componentClasses;
    private static Set<Method> beanMethods;

    public static void scanAndFill(Class<?> baseClass) {
        scanComponentsAndBeans(baseClass);
        fillContainer();
    }

    private static void scanComponentsAndBeans(Class<?> baseClass) {
        Reflections reflections = new Reflections(baseClass.getPackageName(), Scanners.TypesAnnotated, Scanners.MethodsAnnotated);
        componentClasses = reflections.getTypesAnnotatedWith(Component.class);
        beanMethods = reflections.getMethodsAnnotatedWith(Bean.class);

        // @Component가 붙은 클래스에 대해 모두 확인
        scanComponents();

        // @Bean이 붙은 메소드에 대해 모두 확인
        scanBeans();
    }

    private static void scanComponents() {
        for (Class<?> component : componentClasses) {
            // 정의한 생성자가 없는 경우
            if (component.getDeclaredConstructors().length == 0) {
                putConstructor(component, component.getConstructors()[0]);
                continue;
            }

            // 정의한 생성자가 1개만 있는 경우
            if (component.getDeclaredConstructors().length == 1) {
                putConstructor(component, component.getDeclaredConstructors()[0]);
                continue;
            }

            // 정의한 생성자가 여러 개인 경우
            putConstructor(component,
                    Arrays.stream(component.getDeclaredConstructors())
                            .filter(c -> c.isAnnotationPresent(Primary.class))
                            .findFirst()
                            .orElseThrow(() -> new ComponentNotFoundException("정의한 생성자가 여러 개 있지만 @Primary를 사용한 생성자가 없어 구분이 안됩니다.")));
        }
    }

    private static void putConstructor(Class<?> component, Constructor<?> constructor) {
        // 현재 Component에 대한 정보로 넣음
        EXECUTABLES.put(component.getName(), constructor);

        // 구현한 인터페이스에 관련해서도 넣음 (1 depth 까지만)
        Arrays.stream(component.getInterfaces()).forEach(i -> {
            EXECUTABLES.put(i.getName(), constructor);
        });
    }

    private static void scanBeans() {
        for (Method bean : beanMethods) {
            EXECUTABLES.put(bean.getReturnType().getName(), bean);
        }
    }

    private static void fillContainer() {
        // 실행시켜야할 Executable들을 모두 실행시킬 때까지 반복
        while (EXECUTABLES.size() != CONTAINER.size()) {
            int currContainerSize = CONTAINER.size();

            putAllInstancesInContainer();

            if (currContainerSize == CONTAINER.size()) {
                throw new CannotFillContainerException();
            }
        }
    }

    private static void putAllInstancesInContainer() {
        for (Map.Entry<String, Executable> entry : EXECUTABLES.entrySet()) {
            String key = entry.getKey();

            if (CONTAINER.containsKey(key)) {
                continue;
            }

            Executable executable = entry.getValue();
            Parameter[] parameters = executable.getParameters();

            // 해당 Executable에 필요한 매개변수가 있는데 @Autowired가 붙어있지 않으면 의존성 주입을 못해주므로 예외 발생
            if (parameters.length != 0 && !executable.isAnnotationPresent(Autowired.class)) {
                throw new CannotFillContainerException();
            }

            Object[] arguments = new Object[parameters.length];

            if (!fillArguments(parameters, arguments)) {
                continue;
            }

            putInstanceInContainer(key, executable, arguments);
        }
    }

    private static boolean fillArguments(Parameter[] parameters, Object[] arguments) {
        for (int i = 0, size = parameters.length; i < size; i++) {
            String typeName = parameters[i].getType().getName();

            if (!CONTAINER.containsKey(typeName)) {
                return false;
            }

            arguments[i] = CONTAINER.get(typeName);
        }

        return true;
    }

    private static void putInstanceInContainer(String key, Executable executable, Object[] arguments) {
        if (executable instanceof Method) {
            String className = executable.getDeclaringClass().getName();

            if (!CONTAINER.containsKey(className)) {
                return;
            }

            Object instance = CONTAINER.get(className);

            try {
                CONTAINER.put(key, ((Method) executable).invoke(instance, arguments));
            } catch (Exception e) {
                throw new MethodNotFoundException(executable.getName() + ": 해당 메소드를 실행할 수 없습니다.");
            }
        }

        if (executable instanceof Constructor) {
            try {
                CONTAINER.put(key, ((Constructor<?>) executable).newInstance(arguments));
            } catch (Exception e) {
                throw new ConstructorNotFoundException();
            }

        }

        LOGGER.debug("Add instance in container: {}", key);
    }

    public static Object getInstanceFromContainer(String className) {
        if (!CONTAINER.containsKey(className)) {
            throw new ClassNotFoundException();
        }

        return CONTAINER.get(className);
    }

    public static Object getInstanceFromContainer(Class<?> clazz) {
        return getInstanceFromContainer(clazz.getName());
    }
}
