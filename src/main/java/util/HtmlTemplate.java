package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class HtmlTemplate {
    public static StringBuilder includeHtml(String fileName) throws IOException {
        System.out.println(fileName);
        List<String> fileData = Files.readAllLines(new File("./webapp/" + fileName + ".html").toPath());
        StringBuilder body = new StringBuilder();

        for(String line: fileData){
            line = line.trim();

            body.append(line);
        }
        return body;
    }
}
