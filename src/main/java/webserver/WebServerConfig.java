package webserver;

import webserver.http.HttpVersion;

public class WebServerConfig {

    public static final String BASE_PATH = "./webapp";
    public static final String ROOT_PATH = "/";
    public static final String ENTRY_FILE = "/index.html";
    public static final HttpVersion RESPONSE_HTTP_VERSION = new HttpVersion("HTTP/1.1");

}
