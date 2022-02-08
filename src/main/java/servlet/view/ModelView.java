package servlet.view;

import http.message.ResponseMessage;
import http.startline.HttpStatus;
import template.ParseStrings;
import template.Templates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModelView implements View {
    private final String path;
    private final Model model;

    public ModelView(String path, Model model) {
        this.path = path;
        this.model = model;
    }

    @Override
    public ResponseMessage render() {
        try {
            String string = Files.readString(Path.of(path));
            ParseStrings parseStrings = ParseStrings.parse(string);
            Templates templates = parseStrings.createTemplates(model);
            return ResponseMessage.create(HttpStatus.OK, templates.concat(model));
        } catch (IOException e) {
            return ResponseMessage.create(HttpStatus.NOT_FOUND, new byte[]{});
        }
    }
}
