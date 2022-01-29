import annotation.GetMapping;
import org.junit.jupiter.api.Test;
import web.controller.UserController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ReflectionMethodTest {

    @Test
    void test() throws NoSuchMethodException {
        HashMap<String, String> userDto = new HashMap<>();
        userDto.put("userId", "userId");
        userDto.put("name", "name");
        userDto.put("email", "email");
        userDto.put("password", "password");

        Map<String, Method> map = makeMethodMap();
        Method method = map.get("GET/user/create");

        // parameter - fields
        Map<Constructor<?>, Map<String, Class<?>>> test = makeMap(method);

        for(var parameter : test.entrySet()) {
            try {
                parameter.getKey().newInstance(makeParameterWithDto( parameter.getValue().keySet(), userDto));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Method> makeMethodMap() {
        return Arrays.stream(UserController.class.getDeclaredMethods())
                .collect(Collectors.toMap(this::getMappingKey, method -> method));
    }

    private Map<Constructor<?>, Map<String, Class<?>>> makeMap(Method method) throws NoSuchMethodException {
        Map<Constructor<?>, Map<String, Class<?>>> map = new LinkedHashMap<>();
        for(var parameter : method.getParameterTypes()) {
            Map<String, Class<?>> fields = getFields(parameter.getDeclaredFields());
            Constructor<?> constructor = parameter.getDeclaredConstructor(fields.values().toArray(Class<?>[]::new));
            map.put(constructor, fields);
        }
        return map;
    }

    private Map<String, Class<?>> getFields(Field[] fields) {
        Map<String, Class<?>> fieldMap = new LinkedHashMap<>();
        for (Field field : fields)
            fieldMap.put(field.getName(), field.getType());
        return fieldMap;
    }

    private Object[] makeParameterWithDto(Set<String> keySet, HashMap<String, String> userDto) {
        return keySet.stream().map(userDto::get).toArray(Object[]::new);
    }

    private String getMappingKey(Method method) {
        String key = "";
        if (method.isAnnotationPresent(GetMapping.class)) {
            key = "GET"+ method.getAnnotation(GetMapping.class).value();
        }
        return key;
    }
}
