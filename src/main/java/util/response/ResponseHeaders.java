package util.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ResponseHeaders {
    private static final String LINE_CHANGE_STRING = "\r\n";

    private final ContentType contentType;
    private int contentLength = 0;
    private Map<String, String> others = new HashMap<>();

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

    public ResponseHeaders with(Map<String, String> others){
        this.others = others;
        return this;
    }

    public void addHeader(String key, String val){
        this.others.put(key, val);
    }
}
