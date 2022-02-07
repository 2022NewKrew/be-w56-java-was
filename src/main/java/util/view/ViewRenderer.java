package util.view;

import util.response.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class ViewRenderer {
    private static final String STATIC_FILE_BASE_DIRECTORY = "./webapp";
    private static final String LOOP_START_STRING = "{{for";
    private static final String LOOP_END_STRING = "{{for-end}}";

    public static String getRenderedView(ModelAndView modelAndView)
            throws IOException, NoSuchFieldException, IllegalAccessException {

        if(modelAndView == null || modelAndView.getViewName() == null){
            return "";
        }

        String filePath = String.format("%s%s", STATIC_FILE_BASE_DIRECTORY, modelAndView.getViewName());
        String fileString = Files.readString(new File(filePath).toPath());
        return addModel(fileString, modelAndView.getModel());
    }

    private static String addModel(String fileString, Map<String, Object> model)
            throws NoSuchFieldException, IllegalAccessException {

        if(!fileString.contains(LOOP_START_STRING)){
            return fileString;
        }

        StringBuilder sb = new StringBuilder(fileString);

        int loopStart = sb.indexOf(LOOP_START_STRING);
        int modelStart = loopStart + LOOP_START_STRING.length() + 1;
        int modelEnd = sb.indexOf("}}", modelStart);
        int loopEnd = sb.indexOf(LOOP_END_STRING) + LOOP_END_STRING.length();

        String modelName = sb.substring(modelStart, modelEnd);
        String loopContent = sb.substring(modelEnd + 2, loopEnd - LOOP_END_STRING.length());

        List<Object> values = (List<Object>) model.get(modelName);

        sb.replace(loopStart, loopEnd, getReplacedLoop(values, loopContent));
        return sb.toString();
    }

    private static String getReplacedLoop(List<Object> values, String loopContent)
            throws NoSuchFieldException, IllegalAccessException {

        StringBuilder loopSb = new StringBuilder();
        for(int i=0; i<values.size(); i++){
            loopSb.append(getOneReplacedLoop(i+1, values.get(i), loopContent));
        }
        return loopSb.toString();
    }

    private static String getOneReplacedLoop(int loopIdx, Object value, String loopContent)
            throws NoSuchFieldException, IllegalAccessException {

        StringBuilder oneLoopSb = new StringBuilder(loopContent);
        while(oneLoopSb.indexOf("{{") != -1) {
            int valueStart = oneLoopSb.indexOf("{{") + 2;
            int valueEnd = oneLoopSb.indexOf("}}");
            String valueString = oneLoopSb.substring(valueStart, valueEnd);
            oneLoopSb.replace(valueStart - 2, valueEnd + 2, getOneReplacedValue(loopIdx, valueString, value));
        }
        return oneLoopSb.toString();
    }

    private static String getOneReplacedValue(int loopIdx, String valueString, Object value)
            throws NoSuchFieldException, IllegalAccessException {

        if (valueString.equals("for-num")) {
            return String.valueOf(loopIdx);
        }

        Field field = value.getClass().getDeclaredField(valueString);
        field.setAccessible(true);
        return field.get(value).toString();
    }
}