package util.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Builder
@Getter
public class ResponseHeaders {
    private static final String LINE_CHANGE_STRING = "\r\n";

    private ContentType contentType;

    @Builder.Default
    private int contentLength = 0;

    @Builder.Default
    private Map<String, String> others = Collections.emptyMap();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Content-Type: ".concat(contentType.getAcceptName()).concat(";").concat(LINE_CHANGE_STRING));
        sb.append("Content-Length: ".concat(String.valueOf(contentLength)).concat(LINE_CHANGE_STRING));

        for (Map.Entry<String, String> entry : others.entrySet()){
            sb.append(String.format("%s: %s%s", entry.getKey(), entry.getValue(), LINE_CHANGE_STRING));
        }

        return sb.toString();
    }

    public ResponseHeaders with(int contentLength){
        this.contentLength = contentLength;
        return this;
    }
}
