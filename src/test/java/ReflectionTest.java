import annotation.GetMapping;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletContainer;
import web.controller.UserController;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionTest {

    @Test
    void test() throws InvocationTargetException, IllegalAccessException {
        HashMap<String, String> userDto = new HashMap<>();
        userDto.put("userId", "userId");
        userDto.put("name", "name");
        userDto.put("email", "email");
        userDto.put("password", "password");

        Logger log = LoggerFactory.getLogger(this.getClass());
        Map<String, Method> map = methodMap();
        Method method = map.get("GET/user/create");
        String url = (String) method.invoke(new UserController(), getParameters(method, userDto));
        log.info("url : {}", url);
    }

    private Object[] getParameters(Method method, HashMap<String, String> userDto) {
        return Arrays.stream(method.getParameterTypes())
                .map(parameter -> makeParameter(parameter, userDto)).toArray(Object[]::new);
    }

    private Object makeParameter(Class<?> parameter, HashMap<String, String> userDto) {
        Map<String, Class<?>> fields = getFields(parameter.getDeclaredFields());
        try {
            Class<?>[] classes = fields.values().toArray(Class<?>[]::new);
            Constructor<?> cs = parameter.getDeclaredConstructor(classes);
            return cs.newInstance(makeParameterWithDto(fields.keySet(), userDto));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new Object();
    }

    private Object[] makeParameterWithDto(Set<String> keySet, HashMap<String, String> userDto) {
        return keySet.stream().map(userDto::get).toArray(Object[]::new);
    }

    private Map<String, Method> methodMap() {
        return Arrays.stream(UserController.class.getDeclaredMethods())
                .collect(Collectors.toMap(this::getMappingKey, method -> method));
    }

    private String getMappingKey(Method method) {
        String key = "";
        if (method.isAnnotationPresent(GetMapping.class)) {
            key = "GET"+ method.getAnnotation(GetMapping.class).value();
        }
        return key;
    }

    private Map<String, Class<?>> getFields(Field[] fields) {
        Map<String, Class<?>> fieldMap = new LinkedHashMap<>();
        for (Field field : fields)
            fieldMap.put(field.getName(), field.getType());
        return fieldMap;
    }
}
