package dynamic;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DynamicHtmlBuilder {
    private static final String OPEN_SIGN = "\\{\\{";
    private static final String CLOSE_SIGN = "}}";
    private static final String OPEN_REPEAT_START = "\\{\\{#";
    private static final String CLOSE_REPEAT_START = "#}}";
    private static final String OPEN_REPEAT_END = "\\{\\{/";
    private static final String CLOSE_REPEAT_END = "/}}";


    private DynamicHtmlBuilder() {

    }

    public static byte[] getDynamicHtml(byte[] byteBody) {
        return byteBody;
    }

    public static byte[] getDynamicHtml(byte[] byteBody, DynamicModel model) {
        String body = new String(byteBody);
        body = body.replace("{{ #", "{{#");
        body = body.replace("{{ /", "{{/");
        body = body.replace("# }}", "#}}");
        body = body.replace("/ }}", "/}}");

        String builded = buildDynamicHtml(body, model);
        builded = builded.replace("%40", "@");
        return builded.getBytes(StandardCharsets.UTF_8);
    }

    private static String buildDynamicHtml(String body, DynamicModel model) {
        if (body.contains("{{#")) {
            String[] repeatBody = body.split(OPEN_REPEAT_START);
            StringBuilder stringBuilder = new StringBuilder(repeatBody[0]);
            for (int repeatCount = 1; repeatCount < repeatBody.length; repeatCount++) {
                String[] repeatToken = repeatBody[repeatCount].split(CLOSE_REPEAT_START);
                stringBuilder.append(repeatBody(repeatToken[1], model, repeatToken[0]));
            }
            return stringBuilder.toString();
        }
        return buildWithNoRepeat(body, model);
    }

    private static String buildWithNoRepeat(String body, DynamicModel model) {
        if (body.equals("")) return "";
        String[] splitSign = body.split(OPEN_SIGN);
        StringBuilder stringBuilder = new StringBuilder(splitSign[0]);
        for (int signCount = 1; signCount < splitSign.length; signCount++) {
            String[] signBody = splitSign[signCount].split(CLOSE_SIGN);
            stringBuilder.append(model.getAttribute(signBody[0]));
            stringBuilder.append(signBody[1]);
        }
        return stringBuilder.toString();
    }

    private static String repeatBody(String body, DynamicModel model, String repeatList) {
        List<Object> list = (List<Object>) model.getAttribute(repeatList);
        String last = "";
        if (body.contains("{{/")) {
            String[] inRepeat = body.split(OPEN_REPEAT_END);
            body = inRepeat[0];
            last = inRepeat[1].split(CLOSE_REPEAT_END)[1];
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : list) {
            try {
                String className = object.getClass().toString().split(" ")[1];
                String[] splitSign = body.split(OPEN_SIGN);
                stringBuilder.append(splitSign[0]);
                for (int signCount = 1; signCount < splitSign.length; signCount++) {
                    String[] signBody = splitSign[signCount].split(CLOSE_SIGN);
                    if (signBody[0].contains("this.")) {
                        String fieldName = signBody[0].split("this\\.")[1].trim();
                        Class objectClass = Class.forName(className);
                        Field field = objectClass.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        stringBuilder.append(field.get(object));
                        stringBuilder.append(signBody[1]);
                        continue;
                    }
                    stringBuilder.append(model.getAttribute(signBody[0]));
                    stringBuilder.append(signBody[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append(buildWithNoRepeat(last, model));
        return stringBuilder.toString();
    }
}
