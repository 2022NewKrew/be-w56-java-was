package util;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseUtils {

    public static void res(HttpResponse httpResponse, DataOutputStream dos) throws IOException {
        dos.writeBytes(httpResponse.headerText());
        if(httpResponse.getBody() != null) {
            byte[] body = httpResponse.getBody();
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}
