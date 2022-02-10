package util;

import DTO.ModelAndView;
import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicHtmlParsingUtils {
    private static final Logger log = LoggerFactory.getLogger(DynamicHtmlParsingUtils.class);
    final static String htmlDynamicListPattern = "\\{\\{#([\\w]+)\\}\\}"+"(.*?)"+"\\{\\{\\/([\\w]+)\\}\\}";
    final static String elementPattern = "(?<!/)"+ "\\{\\{([a-zA-Z]*?)\\}\\}";

    public static byte[] fillDynamicHtml(byte[] bytes, ModelAndView model){

        String body = byteToString(bytes);
        body = fillListSections(body, model);
        return stringToBytes(body);
    }

    public static String fillListSections(String body, ModelAndView model){
        StringBuilder sb = new StringBuilder();
        int pointer = 0;
        Pattern p = Pattern.compile(htmlDynamicListPattern, Pattern.DOTALL);
        Matcher m = p.matcher(body);

        while(m.find()){
            pointer = buildPerListSection(body, sb, pointer, m, model);
        }
        sb.append(body, pointer, body.length());//add rest string

        return sb.toString();
    }

    private static int fillListSection(Matcher m, ModelAndView modelList, StringBuilder sb){
        // assert that m.group(1).equals(m.group(3))
        if (! m.group(1).equals(m.group(3))){
            log.debug("[HtmlParser] html parsing error: list element is not written properly in HTML!");
            return m.start(1);
        }

        String listParam = m.group(1);
        String dynamicHtml = m.group(2);
        List<Model> models = getModelList(listParam, modelList);

        for(Model model : models){
            String newHtml = fillElement(dynamicHtml, model);
            sb.append(newHtml);
        }

        return m.end();
    }

    private static List<Model> getModelList(String param, ModelAndView model){
        return model.getModelList(param);
    }


    public static String fillElement(String body, Model model){ // ModelAndView model

        Pattern p = Pattern.compile(elementPattern, Pattern.DOTALL);
        Matcher m = p.matcher(body);
        StringBuilder sb = new StringBuilder();
        int pointer = 0;

        while(m.find()){
            String param = m.group(1);
            String value = getValue(model, param);

            buildPerParam(body, sb, value, pointer, m.start());
            pointer = m.end();

        }
        sb.append(body, pointer, body.length());

        return sb.toString();
    }

    private static String getValue(Model model, String param){
        try{
            return model.getParam(param);
        }catch(IllegalArgumentException e){
            log.error(e.getMessage());
            return "{{"+param+"}}";
        }
    }

    private static void replaceToValue( StringBuilder sb, String value){
        sb.append(value);
    }

    private static void buildPrev(String originalStr, StringBuilder sb, int pointer, int startIdx){
        sb.append(originalStr, pointer, startIdx); // add string before pattern
    }

    private static void buildPerParam(String originalStr, StringBuilder sb, String value, int pointer, int startIdx){
        buildPrev(originalStr, sb, pointer, startIdx);
        replaceToValue(sb, value);
    }

    private static int buildPerListSection(String body, StringBuilder sb, int pointer, Matcher m, ModelAndView model){
        buildPrev(body, sb, pointer, m.start());
        return fillListSection(m, model, sb);
    }





    // -- Below is helper functions --
    private static String byteToString(byte[] bytes){
        return new String(bytes);
    }

    private static byte[] stringToBytes(String data){
        return data.getBytes();
    }
}
