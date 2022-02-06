package filter;

import com.google.common.net.HttpHeaders;
import exception.IllegalContentTypeException;
import http.common.Mime;
import http.request.RawHttpRequest;

public class ContentTypeFilter implements Filter {

    public void doFilter(RawHttpRequest request) {
        String requestBodyType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (requestBodyType != null && !requestBodyType.equals(Mime.X_URL_FORM_ENCODED.getContentType())) {
            throw new IllegalContentTypeException("Http request body의 content type이 적절하지 않습니다.");
        }
    }
}
