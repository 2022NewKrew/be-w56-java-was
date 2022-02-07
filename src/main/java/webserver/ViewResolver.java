package webserver;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class ViewResolver {
    public void resolve(Response response, ModelAndView mv) throws IOException, IllegalAccessException {
        if(mv.getViewName().contains("redirect:")){
            response.writeRedirectHeader(mv.getViewName().split("redirect:")[1]);
            return;
        }
        byte[] body = render(mv, Files.readAllBytes(new File("./webapp" + mv.getViewName()).toPath()));
        response.writeHeader(body.length, 200);
        response.writeBody(body);
    }

    public byte[] render(ModelAndView mv, byte[] body) throws IllegalAccessException {
        String bodyString = new String(body);
        while(bodyString.contains("{{#")){
            String key = bodyString.substring(bodyString.indexOf("{{#")+3, bodyString.indexOf("}}"));
            int start = bodyString.indexOf("{{#"+key+"}}")+key.length()+5;
            int end = bodyString.indexOf("{{/"+key+"}}");
            String stringToReplace = bodyString.substring(start, end);
            bodyString = bodyString.substring(0, bodyString.indexOf("{{#"+key+"}}")) + findReplacedString(stringToReplace, mv.getAttribute(key)) + bodyString.substring(bodyString.indexOf("{{/"+key+"}}")+key.length()+5);
        }
        return bodyString.getBytes(StandardCharsets.UTF_8);
    }

    private Map<String,Object> findFieldMap(Object object) throws IllegalAccessException {
        Map<String,Object> fieldMap = new HashMap<String,Object>();
        for(Field field : object.getClass().getDeclaredFields()){
            field.setAccessible(true);
            fieldMap.put(field.getName(), field.get(object));
        }
        return fieldMap;
    }

    private String findReplacedString(String stringToReplace, Object value) throws IllegalAccessException {
        StringBuilder replacedString = new StringBuilder();
        if(!(value instanceof List)){
            value = Arrays.asList(value);
        }
        for(Object object : (List)value){
            Map<String,Object> fieldMap = findFieldMap(object);
            String loopString = stringToReplace;
            while(loopString.contains("{{")){
                String key = loopString.substring(loopString.indexOf("{{")+2, loopString.indexOf("}}"));
                loopString = loopString.substring(0, loopString.indexOf("{{")) + fieldMap.get(key) + loopString.substring(loopString.indexOf("}}")+2);
            }
            replacedString.append(loopString);
        }
        return replacedString.toString();
    }
}
