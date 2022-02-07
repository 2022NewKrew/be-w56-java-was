package template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlTemplate implements Template {
    private final ParseString parseString;

    public HtmlTemplate(ParseString parseString) {
        this.parseString = parseString;
    }

    @Override
    public String load() {
        try {
            String filePath = "./webapp/template/" + parseString.parseName() + ".html";
            File file = new File(filePath);
            return Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
