package infrastructure.util;

import infrastructure.model.Model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static infrastructure.config.ServerConfig.DEFAULT_RESOURCE_PATH;

public class HtmlTemplateUtils {

    private final static String LIST_OPEN = "@@";
    private final static String LIST_CLOSE = "%%";

    static public byte[] getView(String file) throws IOException {
        return Files.readAllBytes(new File(DEFAULT_RESOURCE_PATH + file).toPath());
    }

    static public byte[] getView(String viewName, Model model) throws IOException {
        String contents = Files.readString(new File(DEFAULT_RESOURCE_PATH + viewName).toPath());
        contents = model.replaceAll(contents);

        return contents.getBytes(StandardCharsets.UTF_8);
    }

    static public String getView(String file, String listName, List<Model> list) throws IOException {
        String contents = Files.readString(new File(DEFAULT_RESOURCE_PATH + file).toPath());
        String listOpenRegex = new StringBuilder(LIST_OPEN).append(listName).append(LIST_OPEN).toString();
        String listCloseRegex = new StringBuilder(LIST_CLOSE).append(listName).append(LIST_CLOSE).toString();

        int startIndex = 0;
        while (startIndex < contents.length()) {
            startIndex = contents.indexOf(listOpenRegex, startIndex);
            int endIndex = contents.indexOf(listCloseRegex, startIndex);
            if (startIndex < 0 || endIndex < 0) {
                break;
            }
            String origin = contents.substring(startIndex + listOpenRegex.length(), endIndex);

            String converted = list.stream()
                    .map(e -> e.replaceAll(origin))
                    .collect(Collectors.joining(""));

            contents = contents.replaceAll(new StringBuilder(listOpenRegex).append(origin).append(listCloseRegex).toString(), converted);
            startIndex += converted.length();
        }

        return contents;
    }
}
