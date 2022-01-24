package view;

import webserver.config.WebConst;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ViewResolver {

    public static byte[] render(String viewName) throws IOException {
        return Files.readAllBytes(new File(viewName).toPath());
    }
}
