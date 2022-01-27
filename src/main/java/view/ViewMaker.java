package view;

import com.google.common.io.Files;
import exception.NotFoundException;
import java.io.File;
import java.util.Map;

public class ViewMaker {

    public static byte[] getView(String filePath, Map<String, String> model) {
        byte[] body = getFile("./webapp" + filePath);
        if (model.isEmpty()) {
            return body;
        }
        return applyModel(body, model);
    }

    public static byte[] getNotFoundView() {
        return getFile("./webapp/notFound.html");
    }

    public static byte[] getBadRequestView() {
        return getFile("./webapp/badRequest.html");
    }

    public static byte[] getBadRequestView() throws IOException {
        return Files.toByteArray(new File("./webapp/badRequest.html"));
    }

    private static byte[] applyModel(byte[] body, Map<String, String> model) {
        //미구현
        String stringBody = new String(body);
        return stringBody.getBytes();
    }

    private static byte[] getFile(String filePath) {
        try {
            return Files.toByteArray(new File(filePath));
        } catch (Exception exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }
}
