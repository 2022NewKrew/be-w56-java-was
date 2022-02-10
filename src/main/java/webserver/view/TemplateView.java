package webserver.view;

import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.StringUtils;
import webserver.Model;
import webserver.ModelAndView;
import webserver.View;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateView implements View {
    private static final Logger log = LoggerFactory.getLogger(TemplateView.class);
    private static final Pattern PARTIAL_PATTERN = Pattern.compile("\\{\\{>(.*)}}");
    private static final String VARIABLE_REGEX = "([a-zA-Z][a-zA-Z0-9_]*)";
    private static final Pattern VARIABLE_TAG_PATTERN =
            Pattern.compile("\\{\\{" + VARIABLE_REGEX + "}}");
    private static final Pattern SECTION_TAG_START_PATTERN =
            Pattern.compile("\\{\\{#" + VARIABLE_REGEX + "}}");
    private static final String SECTION_TAG_FORMAT = "\\{\\{#%s}}((.|\r\n|\n)*)\\{\\{/%s}}";
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies;
    private byte[] body;

    @Override
    public void render(ModelAndView mv, OutputStream out) {
        String viewName = mv.getViewName();
        if (viewName == null) {
            status = HttpStatus.InternalServerError;
        } else if (viewName.startsWith("redirect:")) {
            status = HttpStatus.Found;
            String location = viewName.replaceFirst("redirect:", "");
            headers.put("Location", location);
        } else {
            status = mv.getStatus();
            try {
                body = getBody(viewName, mv.getModel());
                headers.put("Content-Length", String.valueOf(body.length));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cookies = mv.getCookies();
        try {
            writeResponse(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getBody(String viewName, Model model) throws IOException {
        byte[] file = Files.readAllBytes(Path.of(viewName));
        if (viewName.endsWith(".html")) {
            String htmlFile = new String(file);
            String mergedHtmlFile = processPartials(htmlFile);
            if (model != null) {
                return processModel(mergedHtmlFile, model);
            }
            return mergedHtmlFile.getBytes();
        }
        return file;
    }

    private String processPartials(String htmlFile) throws IOException {
        StringBuilder processedPartialsStringBuilder = new StringBuilder();
        Matcher partialsMatcher = PARTIAL_PATTERN.matcher(htmlFile);
        while (partialsMatcher.find()) {
            Path partialPath = Path.of(partialsMatcher.group(1).trim());
            String partialFile = Files.readString(partialPath);
            partialsMatcher.appendReplacement(processedPartialsStringBuilder, partialFile);
        }
        partialsMatcher.appendTail(processedPartialsStringBuilder);
        return processedPartialsStringBuilder.toString();
    }

    private byte[] processModel(String fileString, Model model) {
        List<String> sectionKeys = getSectionKeys(fileString);
        StringBuilder fileStringBuilder = new StringBuilder(fileString);
        for (String sectionKey : sectionKeys) {
            fileStringBuilder = processSection(fileStringBuilder, sectionKey, model);
        }
        StringBuilder processedHtmlStringBuilder = processTags(fileStringBuilder, model);
        return processedHtmlStringBuilder.toString().getBytes();
    }

    private List<String> getSectionKeys(String fileString) {
        List<String> sectionKeys = new ArrayList<>();
        Matcher sectionKeyMatcher = SECTION_TAG_START_PATTERN.matcher(fileString);
        while (sectionKeyMatcher.find()) {
            sectionKeys.add(sectionKeyMatcher.group(1));
        }
        return sectionKeys;
    }

    private StringBuilder processSection(StringBuilder fileStringBuilder, String sectionKey, Model model) {
        Pattern sectionPattern = Pattern.compile(String.format(SECTION_TAG_FORMAT, sectionKey, sectionKey));
        Matcher sectionMatcher = sectionPattern.matcher(fileStringBuilder);
        if (!sectionMatcher.find()) {
            throw new RuntimeException("Invalid html template of " + sectionKey);
        }
        StringBuilder sectionStringBuilder = new StringBuilder();
        Collection<?> sectionValue = getSectionValueAsCollection(model, sectionKey);
        if (sectionValue == null) {
            sectionMatcher.appendReplacement(sectionStringBuilder, "");
            sectionMatcher.appendTail(sectionStringBuilder);
            return sectionStringBuilder;
        }
        String sectionString = sectionMatcher.group(1);
        String processedSection = getProcessedSection(sectionValue, sectionString);
        sectionMatcher.appendReplacement(sectionStringBuilder, processedSection);
        sectionMatcher.appendTail(sectionStringBuilder);
        return sectionStringBuilder;
    }

    private Collection<?> getSectionValueAsCollection(Model model, String sectionKey) {
        try {
            return (Collection<?>) model.getAttribute(sectionKey);
        } catch (ClassCastException e) {
            log.error("Section key " + sectionKey + " is not iterable");
            throw new RuntimeException();
        }
    }

    private String getProcessedSection(Collection<?> sectionValue, String sectionString) {
        StringBuilder processedSectionStringBuilder = new StringBuilder();
        for (Object value : sectionValue) {
            processedSectionStringBuilder.append(replaceTagInSection(sectionString, value));
        }
        return processedSectionStringBuilder.toString();
    }

    private StringBuilder replaceTagInSection(String sectionString, Object value) {
        Matcher tagMatcher = VARIABLE_TAG_PATTERN.matcher(sectionString);
        StringBuilder sectionStringBuilder = new StringBuilder();
        while (tagMatcher.find()) {
            String variable = tagMatcher.group(1);
            tagMatcher.appendReplacement(sectionStringBuilder, getTagValueInSection(value, variable));
        }
        tagMatcher.appendTail(sectionStringBuilder);
        return sectionStringBuilder;
    }

    private String getTagValueInSection(Object value, String variable) {
        if (value instanceof String) {
            return (String) value;
        }
        try {
            Class<?> clazz = value.getClass();
            Method method = clazz.getDeclaredMethod("get" + StringUtils.capitalize(variable));
            return method.invoke(value).toString();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("No method found");
        }
    }

    private StringBuilder processTags(StringBuilder fileStringBuilder, Model model) {
        Matcher tagMatcher = VARIABLE_TAG_PATTERN.matcher(fileStringBuilder);
        StringBuilder processedStringBuilder= new StringBuilder();
        while (tagMatcher.find()) {
            String key = tagMatcher.group(1);
            Object value = model.getAttribute(key);
            if (value == null) {
                throw new RuntimeException("Invalid null tag");
            }
            tagMatcher.appendReplacement(processedStringBuilder, value.toString());
        }
        tagMatcher.appendTail(processedStringBuilder);
        return processedStringBuilder;
    }

    private void writeResponse(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes(getStatusLine());
        dos.writeBytes(getResponseHeader());
        dos.writeBytes(getCookieString());
        dos.writeBytes("\r\n");
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }

    private String getStatusLine() {
        return String.format("HTTP/1.1 %d %s\r\n", status.getStatusCode(), status);
    }

    private String getResponseHeader() {
        StringBuilder headerString = new StringBuilder();
        for (String key : headers.keySet()) {
            headerString.append(key)
                    .append(": ")
                    .append(headers.get(key))
                    .append("\r\n");
        }
        return headerString.toString();
    }

    private String getCookieString() {
        StringBuilder cookieString = new StringBuilder();
        for (String key : cookies.keySet()) {
            cookieString.append("Set-Cookie: ")
                    .append(key)
                    .append("=")
                    .append(cookies.get(key))
                    .append("; Path=/\r\n");
        }
        return cookieString.toString();
    }
}
