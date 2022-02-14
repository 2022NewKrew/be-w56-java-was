package webserver.http.request.header;

import lombok.Getter;

import java.util.Optional;

@Getter
public class RequestHeader {
    private static final int REQUEST_HEADER_METHOD_INDEX = 0;
    private final String httpMethod;
    private final Url url;
    private final HeaderMap headerMap;
    private final ParameterMap parameterMap;

    public RequestHeader(String inputHeader) {
        httpMethod = getHttpMethodByHeader(inputHeader);
        url = new Url(inputHeader);
        headerMap = new HeaderMap(inputHeader);
        parameterMap = new ParameterMap(url.getFullPath());
    }

    private static String getHttpMethodByHeader(String inputHeader) {
        return inputHeader.split(" ")[REQUEST_HEADER_METHOD_INDEX];
    }

    private Optional<String> getValue(String parameterName) {
        if (headerMap.containsKey(parameterName))
            return Optional.of(headerMap.get(parameterName));
        return Optional.empty();
    }

    public String getUrlPath() {
        return url.getPath();
    }

    public int getContentLength() {
        return 0;
    }
}
