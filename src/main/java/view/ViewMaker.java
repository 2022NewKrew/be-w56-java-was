package view;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ViewMaker {

    public static byte[] getView(String filePath, Map<String, String> model) throws IOException {
        byte[] body = Files.toByteArray(new File("./webapp" + filePath));

        if(model.isEmpty()){
            return body;
        }
        return applyModel(body, model);
    }

    private static byte[] applyModel(byte[] body, Map<String, String> model) {
        //미구현
        String stringBody = new String(body);
        return stringBody.getBytes();
    }

}
