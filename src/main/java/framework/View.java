package framework;

import framework.params.HttpResponse;
import framework.params.Model;
import framework.params.Params;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static framework.constant.PathVariable.STATIC_RESOURCE_BASE_URL;

public class View {

    public void setResponseBody(HttpResponse httpResponse, String viewFileName, Model model) throws IOException {
        File file = new File(STATIC_RESOURCE_BASE_URL.getPath() + viewFileName);
        httpResponse.setMimeType(getMimeType(file));
        httpResponse.setBody(getModelAppliedView(file, model));
    }

    public byte[] getModelAppliedView(File file, Model model) throws IOException {
        // apply model
        return Files.readAllBytes(file.toPath());
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
