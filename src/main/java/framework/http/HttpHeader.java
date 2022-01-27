package framework.http;

public abstract class HttpHeader {
    public static final String HEADER_KEY_VALUE_SPLIT_DELIMITER = ": ";

    public static final String CONNECTION = "Connection";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String DATE = "DATE";
    public static final String LOCATION = "Location";

    public abstract String getValue(String key);
}
