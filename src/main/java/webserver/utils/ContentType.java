package webserver.utils;

import lombok.Builder;

@Builder
public class ContentType {
    private String mime;
    private String charset;
    private String boundary;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(mime);

        if (charset != null) {
            sb.append(";").append(charset);
        }

        if (boundary != null) {
            sb.append(";").append(boundary);
        }

        sb.append("\r\n");

        return sb.toString();
    }
}
