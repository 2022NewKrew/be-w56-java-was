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

        StringBuilder body = new StringBuilder();

        StringBuilder tempSaveBody = null;
        for(String line: fileData){
            line = line.trim();

            if(line.matches("^\\{\\{>.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();
                body.append(HtmlTemplate.includeHtml(subLine));

                continue;
            }

            if(line.matches("^\\{\\{#.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(!Objects.isNull(model.get(subLine))) {
                    tempSaveBody = new StringBuilder(body);

                    body = new StringBuilder();
                }

                continue;
            }

            if(!Objects.isNull(tempSaveBody) && line.matches("^\\{\\{/.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(!Objects.isNull(model.get(subLine))) {
                    tempSaveBody.append(HtmlTemplate.iterHtmlTag(body, model.get(subLine)));
                }

                body = new StringBuilder(tempSaveBody);
                tempSaveBody = null;

                continue;
            }

            body.append(line);
        }

        return new HttpResponseBody(new String(body).getBytes(StandardCharsets.UTF_8));
    }

    public byte[] getBody() {
        return body;
    }
}
