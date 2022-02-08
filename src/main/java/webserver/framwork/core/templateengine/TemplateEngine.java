package webserver.framwork.core.templateengine;

import com.google.gson.*;
import webserver.framwork.core.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TemplateEngine {
    public static final Gson gson = new Gson();

    public static String render(List<String> rawViewBody, Model model) {

        StringBuilder sb = new StringBuilder();

        List<MustacheBlock> lines = new ArrayList<>();
        boolean inScope = false;
        for (String line : rawViewBody) {
            MustacheBlock mustacheBlock = new MustacheBlock(line);

            if (inScope) {
                lines.add(mustacheBlock);

                if (mustacheBlock.isScopeEnd()) {
                    renderScope(lines, model, sb);
                    inScope = false;
                }
                continue;
            }

            if (mustacheBlock.isScopeStart()) {
                lines.clear();
                lines.add(mustacheBlock);
                inScope = true;
                continue;
            }

            sb.append(line);
        }

        return sb.toString();
    }

    public static void renderScope(List<MustacheBlock> lines, Model model, StringBuilder sb) {
        MustacheBlock firstLine = lines.get(0);
        String attrKey = firstLine.getIdentifier();

        if (attrKey.isEmpty() || !model.hasAttribute(attrKey)) {
            throw new NoSuchElementException("그런 키는 없습니다.");
        }

        Object obj = model.getAttribute(attrKey);
        if (obj == null || obj.equals(false)) {
            return;
        }

        String jsonString = gson.toJson(obj);
        JsonElement modelData = JsonParser.parseString(jsonString);

        if (modelData.isJsonArray()) {
            JsonArray dataArray = modelData.getAsJsonArray();
            for (JsonElement jsonElement : dataArray) {
                JsonObject data = jsonElement.getAsJsonObject();
                replaceMustacheWithData(lines.subList(1, lines.size() - 1), data, sb);
            }
            return;
        }

        if (modelData.isJsonObject()) {
            JsonObject data = modelData.getAsJsonObject();
            replaceMustacheWithData(lines.subList(1, lines.size() - 1), data, sb);
        }

        if (modelData.isJsonPrimitive() && modelData.getAsBoolean()) {
            replaceMustacheWithData(lines.subList(1, lines.size() - 1), null, sb);
        }

    }

    public static void replaceMustacheWithData(List<MustacheBlock> lines, JsonObject data, StringBuilder sb) {
        for (MustacheBlock line : lines) {
            if (!line.hasMustache() || data == null) {
                sb.append(line);
                continue;
            }
            String value = data.get(line.getIdentifier()).getAsString();
            sb.append(line.replaceWith(value));
        }
    }


}
