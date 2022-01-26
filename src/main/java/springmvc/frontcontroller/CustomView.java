package springmvc.frontcontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CustomView {
    private String viewPath;

    public CustomView(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getViewPath() {
        return viewPath;
    }

    public byte[] viewToByte() throws IOException {
        return Files.readAllBytes(new File(viewPath).toPath());
    }
}
