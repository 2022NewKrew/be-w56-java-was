package template;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class View {

    private final File file;
    private final Model model;
    private final String contents;

    public View(File file, Model model) throws IOException {
        this.file = file;
        this.model = model;
        this.contents = generateContent();
    }

    private String generateContent() throws IOException {
        String fileContents = Files.readString(file.toPath());
        if (model.isEmpty()) {
            return fileContents;
        }
        return TemplateUtils.mappingModel(fileContents, model);
    }

    public File getFile() {
        return file;
    }

    public String getContents() {
        return contents;
    }
}
