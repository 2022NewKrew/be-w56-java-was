package webserver.http;

public class HttpMeta {

    // For Http Request Decoder
    public static final String SEPARATOR_OF_REQUEST_LINE = " ";
    public static final int INDEX_OF_METHOD_IN_REQUEST_LINE = 0;
    public static final int INDEX_OF_URI_IN_REQUEST_LINE = 1;
    public static final int INDEX_OF_HTTP_VERSION_IN_REQUEST_LINE = 2;

    // For Http Response
    public static final String HTTP_STATUS_OK = "OK";
    public static final String HTTP_STATUS_NOT_OK = "NOT OK";
    public static final String SUFFIX_OF_JS_FILE = ".js";
    public static final String SUFFIX_OF_CSS_FILE = ".css";
    public static final String MIME_TYPE_OF_JAVASCRIPT = "application/javascript";
    public static final String MIME_TYPE_OF_CSS = "text/css";

    // For Http Response Encoder
    public static final String SEPARATOR_OF_STATUS_LINE = " ";
    public static final String SEPARATOR_OF_HEADER_LINE = ": ";
    public static final String SEPARATOR_OF_BETWEEN_HEADERS = "\r\n";
}
