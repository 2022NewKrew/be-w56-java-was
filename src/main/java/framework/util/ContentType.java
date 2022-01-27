package framework.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 요청 헤더의 Content-Type 정보를 담을 클래스
 */
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
