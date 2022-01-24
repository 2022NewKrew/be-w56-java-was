package util;

public class HttpServletRequestUtils {


    public RequestInfo parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        RequestInfo requestInfo = new RequestInfo(tokens[0],tokens[1],tokens[2]);
        return requestInfo;
    }
    public class RequestInfo{
        private String method;
        private String url;
        private String protocol;
        public RequestInfo(String method, String url, String protocol) {
            this.method = method;
            this.url = url;
            this.protocol = protocol;
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public String getProtocol() {
            return protocol;
        }
    }
}
