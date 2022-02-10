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

    /**
     * 템플릿 엔진을 적용하여 파일을 읽는 메서드
     * @return 읽은 파일의 문자열을 반환
     * @throws IOException
     */
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

    /**
     * 각 줄을 읽는 메서드, 루프 안에 들어가지 않는 경우
     * @param line 각 줄
     */
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

    /**
     * 각 줄을 읽는 메서드, 루프 안에 들어간 경우
     * @param line 각 줄
     * @param model 템플릿에 적용할 모델
     */
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

    /**
     * 모델에서 원하는 attribute를 가져오게 하는 메서드 이름을 찾는 메서드
     * @param attr attribute 이름
     * @return getter method 이름
     */
    private String findGetterName(String attr){
        return "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
    }

    /**
     * 모델에서 getter method를 통해 원하는 값을 찾는 메서드
     * @param attr attribute 이름
     * @param model 템플릿에 적용할 모델
     * @return 원하는 attribute 값
     */
    private String findValue(String attr, Object model){
        try{
            String getterName = findGetterName(attr);
            Method method = objClass.getMethod(getterName);
            return (String) method.invoke(model);
        } catch (Exception e){
            throw new TemplateEngineException("모델에서 값을 불러오던 중 오류가 발생했습니다.");
        }
    }

    /**
     * 루프에 진입했는지 판단하는 메서드
     * @param attr 패턴에 일치하는 문자열
     */
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

    /**
     * 루프 구간을 읽는 메서드
     */
    private void readLoopLines(){
        loopLines.remove(LOOP_START_IDX);
        List<?> objects = (List<?>) attribute;
        if(objects.size() > 0){
            objClass = objects.get(LOOP_START_IDX).getClass();
            readObjects(objects);
        }
    }

    /**
     * 객체 수만큼 루프를 도는 메서드
     * @param objects
     */
    private void readObjects(List<?> objects){
        for(Object model : objects){
            readLines(model);
        }
    }

    /**
     * 객체인 모델을 기반으로 루프를 읽는 메서드
     * @param model 템플릿에 적용할 모델
     */
    private void readLines(Object model){
        for(String line : loopLines){
            readLine(line, model);
        }
    }


}
