package util;

import DTO.ModelAndView;
import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {
    private static final Logger log = LoggerFactory.getLogger(HtmlParser.class);
    final static String htmlDynamicListPattern = "(\\{\\{#[\\w]+\\}\\})"+"(.*?)"+"(\\{\\{\\/[\\w]+\\}\\})";
    final static String elementPattern = "(?<!/)"+ "\\{\\{([a-zA-Z]*?)\\}\\}";

    public static byte[] fillDynamicHtml(byte[] bytes){
        String body = byteToString(bytes);
        getListSection(body);
        return bytes; // todo :change to body
    }

    public static String getListSection(String body){

        Pattern p = Pattern.compile(htmlDynamicListPattern, Pattern.DOTALL);
        Matcher m = p.matcher(body);

        while(m.find()){
            String val = m.group();
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));

            String dynamicHtml = m.group(2);
            //fillElement(dynamicHtml, model);

        }
        return body;
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

            System.out.println("elem: "+ param + "->" + value);
        }
        sb.append(body, pointer, body.length());//add rest string

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





    // -- Below is helper functions --
    private static String byteToString(byte[] bytes){
        return new String(bytes);
    }

    private static byte[] stringToBytes(String data){
        return data.getBytes();
    }
}
