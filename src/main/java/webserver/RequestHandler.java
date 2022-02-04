package webserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.MainController;
import lombok.extern.slf4j.Slf4j;
import util.HttpRequestUtils;
import webserver.annotation.RequestMethod;
import webserver.exception.TemplateParsingException;
import webserver.model.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
public class RequestHandler {
    private static final RequestHandler INSTANCE = new RequestHandler();

    public static RequestHandler getInstance() {
        return INSTANCE;
    }

    public RequestHandler() {

    }

    private final MainController controller = MainController.getInstance();

    public void handle(WebHttpRequest httpRequest, WebHttpResponse httpResponse) {
        if (!RequestMapping.isRegistered(httpRequest)) {
            httpResponse.setHttpStatus(HttpStatus.OK);
            setContent(httpResponse, httpRequest.uri().getPath(), new Model());
            return;
        }

        RequestMethod requestMethod = RequestMethod.valueOf(httpRequest.method());
        String uri = httpRequest.uri().getPath();
        Method controllerMethod = RequestMapping.getControllerMethod(requestMethod, uri);
        Model model = new Model();
        Object[] params = setParameters(httpRequest, httpResponse, requestMethod, controllerMethod, model);

        try {
            String result = (String) controllerMethod.invoke(controller, params);
            if (result.startsWith("redirect:")) {
                httpResponse.setHttpStatus(HttpStatus.FOUND);
                httpResponse.setHeaders("Location", result.split(":")[1]);
                return;
            }
            httpResponse.setHttpStatus(HttpStatus.OK);
            setContent(httpResponse, result, model);
        } catch (InvocationTargetException | IllegalAccessException e) {
            httpResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Object[] setParameters(
            WebHttpRequest httpRequest,
            WebHttpResponse httpResponse,
            RequestMethod requestMethod,
            Method controllerMethod,
            Model model) {
        Map<String, String> paramMap = getQueryString(httpRequest, requestMethod);
        paramMap.putAll(getRequestBody(httpRequest, requestMethod));
        Class[] parameters = controllerMethod.getParameterTypes();

        ObjectMapper mapper = new ObjectMapper();
        List<Object> params = new ArrayList<>();
        for (Class type : parameters) {
            if (type == WebHttpRequest.class) {
                params.add(httpRequest);
            } else if (type == WebHttpResponse.class) {
                params.add(httpResponse);
            } else if (type == Model.class) {
                params.add(model);
            } else {
                params.add(mapper.convertValue(paramMap, type));
            }
        }
        return params.toArray();
    }

    private Map<String, String> getQueryString(WebHttpRequest httpRequest, RequestMethod requestMethod) {
        return HttpRequestUtils.parseQueryString(httpRequest.uri().getQuery());
    }

    private Map<String, String> getRequestBody(WebHttpRequest httpRequest, RequestMethod requestMethod) {
        return HttpRequestUtils.parseQueryString(httpRequest.getBody());
    }

    private String getMimeType(String pathString) {

        try {
            String extension = pathString.substring(pathString.lastIndexOf("."));
            Path tmpPath = new File("file" + extension).toPath();
            String mimeType = Files.probeContentType(tmpPath);
            if (mimeType == null) {
                if (extension.equals(".woff")) {
                    mimeType = "application/font-woff";
                } else {
                    mimeType = "text/html";
                }
            }
            return mimeType;
        } catch (IOException e) {
            e.printStackTrace();
            return "text/html";
        }
    }

    private void setContent(WebHttpResponse httpResponse, String pathString, Model model) {
        String mimeType = getMimeType(pathString);
        httpResponse.setHeaders("Content-Type", mimeType + ";charset=utf-8");
        Path path = new File("./webapp" + pathString).toPath();
        try {
            if (mimeType.equals("text/html")) {
                List<String> lines = Files.readAllLines(path);
                String bodyString = render(lines, model);
                byte[] body = bodyString.getBytes(StandardCharsets.UTF_8);
                httpResponse.setHeaders("Content-Length", Integer.toString(body.length));
                httpResponse.setBody(body);
            } else {
                byte[] body = Files.readAllBytes(path);
                httpResponse.setHeaders("Content-Length", Integer.toString(body.length));
                httpResponse.setBody(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String render(List<String> lines, Model model) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("\\{\\{([^\\}]*)\\}\\}");
        Deque<List<Object>> sections = new LinkedList<>();
        List<List<String>> templates = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (sections.isEmpty()) {
                if (matcher.find()) {
                    sb.append(line.substring(0, matcher.start()));
                    String tag = matcher.group();
                    if (tag.startsWith("{{#")) {
                        String sectionTag = tag.substring(3, tag.length() - 2);
                        List<Object> objectList = (List<Object>) model.getAttribute(sectionTag);
                        sections.addLast(objectList);
                        templates = new ArrayList<>();
                    } else if (tag.startsWith("{{/")) {
                        throw new TemplateParsingException();
                    } else {
                        String variable = tag.substring(2, tag.length() - 2);
                        sb.append(model.getAttribute(variable));
                        sb.append(line.substring(matcher.end()));
                    }
                } else {
                    sb.append(line);
                }
                sb.append(System.lineSeparator());
            } else {
                if (matcher.find()) {
                    String tag = matcher.group();
                    if (tag.startsWith("{{#")) {
                        throw new TemplateParsingException();
                    } else if (tag.startsWith("{{/")) {
                        sb.append(line.substring(0, matcher.start()));
                        String section = tag.substring(3, tag.length() - 2);
                        for (int i = 0; i < sections.peek().size(); i++) {
                            for (int j = 0; j < templates.size(); j++) {
                                sb.append(templates.get(j).get(i));
                                sb.append(System.lineSeparator());
                            }
                        }
                        sb.append(line.substring(matcher.end()));
                        sb.append(System.lineSeparator());
                        sections.pollLast();
                    } else {
                        String variable = tag.substring(2, tag.length() - 2);
                        if (variable.equals("index")) {
                            List<String> tmp = new ArrayList<>();
                            for (int idx = 1; idx <= sections.peek().size(); idx++) {
                                String newLine = line.substring(0, matcher.start());
                                newLine += idx;
                                newLine += line.substring(matcher.end());
                                tmp.add(newLine);
                            }
                            templates.add(tmp);
                        } else { // 일반 패턴 - 인덱스 아님
                            List<String> tmp = new ArrayList<>();
                            for (int idx = 0; idx < sections.peek().size(); idx++) {
                                String newLine = line.substring(0, matcher.start());
                                try {
                                    Class<?> clazz = sections.peek().get(idx).getClass();
                                    Field field = clazz.getDeclaredField(variable);
                                    field.setAccessible(true);
                                    newLine += field.get(sections.peek().get(idx)).toString();
                                } catch (NoSuchFieldException e) { //이게 발생하네
                                    throw new TemplateParsingException();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                newLine += line.substring(matcher.end());
                                tmp.add(newLine);
                            }
                            templates.add(tmp);
                        }
                    }
                } else { // 패턴매칭 실패
                    List<String> tmp = new ArrayList<>();
                    IntStream.range(0, sections.peek().size()).forEach(idx -> {
                        tmp.add(line);
                    });
                    templates.add(tmp);
                }
            }
        }
        return sb.toString();
    }
}
