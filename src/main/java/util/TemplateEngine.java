package util;

import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.exception.TemplateEngineException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateEngine {
    public static final Logger log = LoggerFactory.getLogger(TemplateEngine.class);
    private static final String PATTERN = "\\{\\{.+?}}";
    private static final String LOOP_PATTERN = "\\{\\{#.+?}}";
    private static final String END_PATTERN = "\\{\\{/.+?}}";
    private static final int COVER_LENGTH = 2;
    private static final int LOOP_SIGN_OFFSET = 1;
    private static final int LOOP_START_IDX = 0;

    private final Pattern p = Pattern.compile(PATTERN);
    private final BufferedReader br;
    private final ModelAndView mv;
    private final StringBuilder sb = new StringBuilder();

    private Object attribute;
    private boolean loopSign = false;
    private final ArrayList<String> loopLines = new ArrayList<>();
    private Class<?> objClass;

    public TemplateEngine(BufferedReader br, ModelAndView mv){
        this.br = br;
        this.mv = mv;
    }

    public String read() throws IOException {
        log.info("[TemplateEngine] -------------read------------");
        String line;
        while((line = br.readLine()) != null){
            checkLoopSign(line);
            if(loopSign){
                loopLines.add(line);
            }
            else if(!Pattern.matches(END_PATTERN, line.trim())){
                readLine(line);
            }
        }
        return sb.toString();
    }

    private void readLine(String line){
        Matcher matcher = p.matcher(line);
        while(matcher.find()){
            String attr = matcher.group();
            attr = attr.substring(COVER_LENGTH, attr.length()-COVER_LENGTH).trim();
            log.info("[TemplateEngine] ReadLine without Model : " + attr);
            line = line.replaceFirst(PATTERN, (String) mv.getValue(attr));
        }
        sb.append(line);
    }

    private void readLine(String line, Object model) {
        Matcher matcher = p.matcher(line);
        while(matcher.find()){
            String attr = matcher.group();
            attr = attr.substring(COVER_LENGTH, attr.length()-COVER_LENGTH).trim();
            log.info("[TemplateEngine] ReadLine with Model : " + attr);
            line = line.replaceFirst(PATTERN, findValue(attr, model));
        }
        sb.append(line);
    }

    private String findGetterName(String attr){
        return "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
    }

    private String findValue(String attr, Object model){
        try{
            String getterName = findGetterName(attr);
            Method method = objClass.getMethod(getterName);
            return (String) method.invoke(model);
        } catch (Exception e){
            throw new TemplateEngineException("모델에서 값을 불러오던 중 오류가 발생했습니다.");
        }
    }

    private void checkLoopSign(String attr){
        attr = attr.trim();
        if(Pattern.matches(LOOP_PATTERN, attr)){
            attr = attr.substring(COVER_LENGTH + LOOP_SIGN_OFFSET, attr.length()-COVER_LENGTH);
            log.info("[TemplateEngine] Get Attribute : " + attr);
            attribute = mv.getValue(attr);
            loopSign = true;
        }
        if(Pattern.matches(END_PATTERN, attr)){
            readLoopLines();
            loopLines.clear();
            objClass = null;
            attribute = null;
            loopSign = false;
        }
    }

    private void readLoopLines(){
        loopLines.remove(LOOP_START_IDX);
        log.info(loopLines.toString());
        List<?> objects = (List<?>) attribute;
        if(objects.size() > 0){
            objClass = objects.get(LOOP_START_IDX).getClass();
            readObjects(objects);
        }
    }

    private void readObjects(List<?> objects){
        for(Object model : objects){
            readLines(model);
        }
    }

    private void readLines(Object model){
        for(String line : loopLines){
            readLine(line, model);
        }
    }


}
