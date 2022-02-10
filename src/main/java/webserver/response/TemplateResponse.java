package webserver.response;


import com.beust.jcommander.internal.Maps;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.TemplateProcessingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateResponse extends Response {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(TemplateResponse.class);

    private static final String TEMPLATE_ROOT_PATH = "./webapp/template/";
    private static final String TEMPLATE_EXTENSION = ".html";
    private static final String TEMPLATE_MIMETYPE = "text/html";
    private File template;

    TemplateResponse(String path) throws TemplateProcessingException {
        super(StatusCode.OK);
        validateTemplateFile(path);
        try {
            setContents(TEMPLATE_MIMETYPE, Files.readAllBytes(template.toPath()));
        } catch (IOException e) {
            throw new TemplateProcessingException("template file 읽기 실패");
        }
    }

    private void validateTemplateFile(String path) throws TemplateProcessingException {
        try {
            File file = new File(TEMPLATE_ROOT_PATH + path + TEMPLATE_EXTENSION);
            template = file;
            if (!file.exists()) {
                throw new TemplateProcessingException("template file이 없음");
            }
            if (!(new Tika().detect(file).equals(TEMPLATE_MIMETYPE))) {
                throw new TemplateProcessingException("template file이 html이 아님");
            }
        } catch (IOException e) {
            throw new TemplateProcessingException("template file이 이상해요");
        }
    }

    public void parseTemplate(Model model) throws TemplateProcessingException {
        if (!template.exists()) {
            throw new TemplateProcessingException("template file 어디감??");
        }
        try (BufferedReader inFile = new BufferedReader(new FileReader(template))) {
            StringBuilder sb = new StringBuilder();
            String sLine = null;
            while ((sLine = inFile.readLine()) != null) {
                if (sLine.matches("(.*)\\{\\{#(.*)\\}\\}(.*)")) {
                    parseTemplateIterator(sLine, model, inFile, sb);
                } else if (sLine.matches("(.*)\\{\\{(.*)\\.(.*)\\}\\}(.*)")) {
                    parseTemplateMap(sLine, model, sb);
                } else if (sLine.matches("(.*)\\{\\{(.*)\\}\\}(.*)")) {
                    parseTemplateRowType(sLine, model, sb);
                } else {
                    sb.append(sLine + "\n");
                }
                //System.out.println(sLine); //읽어들인 문자열을 출력 합니다.
            }
            log.debug(sb.toString());
            setContents(TEMPLATE_MIMETYPE, sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TemplateProcessingException("template file 읽기 실패");
        }
    }

    private void parseTemplateIterator(String sLine, Model model, BufferedReader inFile,
        StringBuilder sb)
        throws TemplateProcessingException {
        try {
            log.debug("iter type: " + sLine);
            String key = StringUtils.substringBetween(sLine, "{{#", "}}");
            List<Map<String, Object>> maps = (List<Map<String, Object>>) model.getAttribute(key);
            StringBuilder temp = new StringBuilder();
            List<String> secKeys = new ArrayList<>();
            while (true) {
                sLine = inFile.readLine();
                if (sLine == null) {
                    throw new TemplateProcessingException("");
                }
                if (sLine.matches("(.*)\\{\\{/" + key + "\\}\\}(.*)")) {
                    break;
                }
                if (sLine.matches("(.*)\\{\\{(.*)\\}\\}(.*)")) {
                    secKeys.add(StringUtils.substringBetween(sLine, "{{", "}}"));
                }
                temp.append(sLine + "\n");
            }
            String content = temp.toString();
            for (Map<String, Object> map : maps) {
                String replaceContents = content;
                for (String secKey : secKeys) {
                    replaceContents = replaceContents.replaceAll("\\{\\{" + secKey + "\\}\\}",
                        map.get(secKey).toString());
                }
                sb.append(replaceContents);
            }
        } catch (Exception e) {
            throw new TemplateProcessingException(sLine + " parcing exception");
        }
    }

    private void parseTemplateRowType(String sLine, Model model, StringBuilder sb)
        throws TemplateProcessingException {
        try {
            String str = StringUtils.substringBetween(sLine, "{{", "}}");
            String val = model.getAttribute(str).toString();
            sLine = sLine.replaceFirst("\\{\\{(.*)\\}\\}", val);
            sb.append(sLine + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TemplateProcessingException(sLine + " parcing exception");
        }
    }

    private void parseTemplateMap(String sLine, Model model, StringBuilder sb)
        throws TemplateProcessingException {
        try {
            String key = StringUtils.substringBetween(sLine, "{{", ".");
            String secKey = StringUtils.substringBetween(sLine, ".", "}}");
            String val = ((Map<String, Object>) model.getAttribute(key)).get(secKey).toString();
            sLine = sLine.replaceFirst("\\{\\{(.*)\\}\\}", val);
            sb.append(sLine + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            throw new TemplateProcessingException(sLine + " parcing exception");
        }
    }

    public static class Model {

        Map<String, Object> attributes;

        public Model() {
            attributes = Maps.newHashMap();
        }

        public void addAttribute(String key, Object object) throws TemplateProcessingException {
            if (object instanceof Iterable) {
                Iterable iterable = (Iterable) object;
                System.out.println(iterable);
                List<Map<String, Object>> val = new ArrayList<>();
                for (var elem : iterable) {
                    try {
                        Map<String, Object> map = objectMapper.convertValue(elem, Map.class);
                        val.add(map);
                    } catch (IllegalArgumentException e) {
                        throw new TemplateProcessingException(
                            "Model.addAttribute: " + elem);
                    }
                }
                attributes.put(key, val);
                return;
            }
            try {
                Map<String, Object> map = objectMapper.convertValue(object, Map.class);
                attributes.put(key, map);
            } catch (IllegalArgumentException e) {
                attributes.put(key, object);
            }
        }

        public Object getAttribute(String key) {
            return attributes.get(key);
        }
    }
}
