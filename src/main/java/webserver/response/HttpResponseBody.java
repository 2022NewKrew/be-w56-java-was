package webserver.response;

import mapper.HtmlUseConst;
import mapper.MappingConst;
import mapper.ResponseSendDataModel;
import util.HtmlTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class HttpResponseBody {
    private final byte[] body;

    private HttpResponseBody(byte[] body) {
        this.body = body;
    }

    public HttpResponseBody(){
        this.body = "".getBytes();
    }

    public static HttpResponseBody makeHttpResponseBody(ResponseSendDataModel model) throws IOException {
        String fileName = model.getName();

        if(fileName.matches("^" + MappingConst.FONT + ".*") || fileName.matches("^" + MappingConst.ICON + ".*")){
            byte[] bytes = Files.readAllBytes(new File("./webapp" + fileName).toPath());

            return new HttpResponseBody(bytes);
        }

        List<String> fileData = Files.readAllLines(new File("./webapp" + fileName).toPath());

        StringBuilder body = HtmlTemplate.dynamicHtmlParsing(fileData, model);

        return new HttpResponseBody(new String(body).getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getBody() {
        return body;
    }
}
