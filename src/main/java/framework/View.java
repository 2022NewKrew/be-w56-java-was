package framework;

import framework.params.HttpResponse;
import framework.params.Model;
import framework.util.TemplateParser;
import org.apache.tika.Tika;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

import static framework.constant.PathVariable.STATIC_RESOURCE_BASE_URL;

public class View {
    private final TemplateParser templateParser;

    public View(TemplateParser templateParser) {
        this.templateParser = templateParser;
    }

    public void setResponseBody(HttpResponse httpResponse, String viewFileName, Model model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        File file = new File(STATIC_RESOURCE_BASE_URL.getPath() + viewFileName);
        httpResponse.setMimeType(getMimeType(file));
        httpResponse.setBody(getModelAppliedView(file, model));
    }

    public byte[] getModelAppliedView(File file, Model model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return templateParser.getModelAppliedView(file, model);
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
