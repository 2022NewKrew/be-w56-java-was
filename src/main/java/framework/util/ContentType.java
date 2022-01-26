package framework.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentType {
    private String mime;
    private String charset;
    private String boundary;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (mime != null) {
            sb.append(mime);
        }

        if (charset != null) {
            sb.append(";").append(charset);
        }

        if (boundary != null) {
            sb.append(";").append(boundary);
        }

        return sb.toString();
    }
}
