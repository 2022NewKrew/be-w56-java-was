package webserver.response;

import mapper.MappingConst;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class HttpResponseBody {
    private final byte[] body;

    private HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public static HttpResponseBody makeHttpResponseBody(String fileName) throws IOException {
        if(fileName.matches("^" + MappingConst.FONT + ".*") || fileName.matches("^" + MappingConst.ICON + ".*")){
            byte[] bytes = Files.readAllBytes(new File("./webapp" + fileName).toPath());
            System.out.println(fileName + bytes.length);
            return new HttpResponseBody(bytes);
        }

        List<String> fileData = Files.readAllLines(new File("./webapp" + fileName).toPath());

        StringBuilder body = new StringBuilder();

        for(String line: fileData){
            body.append(line);
        }

        return new HttpResponseBody(new String(body).getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getBody() {
        return body;
    }
}
