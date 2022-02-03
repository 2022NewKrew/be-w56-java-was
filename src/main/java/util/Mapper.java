package util;

import http.HttpMethod;
import http.RequestMessage;
import servlet.ServletRequest;

public class Mapper {

    private Mapper() {
    }

    public static ServletRequest toServletRequest(RequestMessage request) {
        if (request.getRequestLine().getMethod() == HttpMethod.POST) {
            return new ServletRequest(
                    request.getRequestLine().getMethod(),
                    request.getRequestLine().getRequestTarget().getPath().getValue(),
                    request.getRequestBody().getQueryParameters().getParameters(),
                    request.getHeader().createCookie()
            );
        }

        return new ServletRequest(
                request.getRequestLine().getMethod(),
                request.getRequestLine().getRequestTarget().getPath().getValue(),
                request.getRequestLine().getRequestTarget().getQueryParameters().getParameters(),
                request.getHeader().createCookie()
        );
    }
}
