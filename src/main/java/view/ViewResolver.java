package view;

import org.apache.tika.Tika;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static framework.variable.PathVariable.BASEURL;
/**
 * FrontController로부터 View 이름을 전달받아 View를 검색
 */
public class ViewResolver {

    public DataOutputStream getResponse(OutputStream out, String viewName) throws IOException {
        if (!viewName.contains(".")) {
            viewName += ".html";
        }

        View view = new View();
        File file = new File(BASEURL.getPath() + viewName);

        view.setMimeType(getMimeType(file));
        view.setBody(Files.readAllBytes(file.toPath()));

        return view.getResponse(out);
    }

    private String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
