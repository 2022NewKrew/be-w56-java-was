package view;

import com.google.common.io.Files;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class View {
    public static byte[] get(String filePath) throws IOException {
        return Files.toByteArray(new File("./webapp" + filePath));
    }

    public static void sendResponse(OutputStream out, byte[] message) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.write(message);
        dos.flush();
    }
}
