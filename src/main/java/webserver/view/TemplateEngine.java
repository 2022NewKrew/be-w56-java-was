package webserver.view;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TemplateEngine {
    private static final String OPEN_OBJECT = "{{#";
    private static final String CLOSE_OBJECT = "{{/";
    private static final String OPEN_FIELD = "{{";
    private static final String CLOSE = "}}";

    public byte[] render(ModelAndView mv, byte[] body) throws IllegalAccessException {
        String bodyString = new String(body);
        while(bodyString.contains(OPEN_OBJECT)){
            String key = bodyString.substring(bodyString.indexOf(OPEN_OBJECT)+OPEN_OBJECT.length(), bodyString.indexOf(CLOSE));
            int start = bodyString.indexOf(OPEN_OBJECT+key+CLOSE);
            int end = bodyString.indexOf(CLOSE_OBJECT+key+CLOSE);
            String stringToReplace = bodyString.substring(start+key.length()+OPEN_OBJECT.length()+CLOSE.length(), end);
            bodyString = bodyString.substring(0, start) + findReplacedString(stringToReplace, mv.getAttribute(key)) + bodyString.substring(end+key.length()+CLOSE_OBJECT.length()+CLOSE.length());
        }
        return bodyString.getBytes(StandardCharsets.UTF_8);
    }

    private Map<String,Object> findFieldMap(Object object) throws IllegalAccessException {
        Map<String,Object> fieldMap = new HashMap<>();
        for(Field field : object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(object));
        }
        return fieldMap;
    }

    private String findReplacedString(String stringToReplace, Object value) throws IllegalAccessException {
        StringBuilder replacedString = new StringBuilder();
        if(!(value instanceof List)){
            value = List.of(value);
        }
        for(Object object : (List<?>)value){
            Map<String,Object> fieldMap = findFieldMap(object);
            String loopString = stringToReplace;
            while(loopString.contains(OPEN_FIELD)){
                String key = loopString.substring(loopString.indexOf(OPEN_FIELD)+OPEN_FIELD.length(), loopString.indexOf(CLOSE));
                loopString = loopString.substring(0, loopString.indexOf(OPEN_FIELD)) + fieldMap.get(key) + loopString.substring(loopString.indexOf(CLOSE)+CLOSE.length());
            }
            replacedString.append(loopString);
        }
        return replacedString.toString();
    }
}
