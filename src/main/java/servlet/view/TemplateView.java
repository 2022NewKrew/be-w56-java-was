package servlet.view;

import http.header.Cookie;
import http.header.MimeType;
import http.message.ResponseMessage;
import http.startline.HttpStatus;
import template.ParseStrings;
import template.Templates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TemplateView implements View {
    private final String path;

    public TemplateView(String path) {
        this.path = "./webapp/template" + path;
    }

    @Override
    public ResponseMessage render(Model model, Cookie cookie) throws IOException {
        String string = Files.readString(Path.of(path));
        ParseStrings parseStrings = ParseStrings.parse(string);
        Templates templates = parseStrings.createTemplates(model);
        return ResponseMessage.create(HttpStatus.OK, MimeType.matchOf(path), templates.concat(), cookie);
    }
}
