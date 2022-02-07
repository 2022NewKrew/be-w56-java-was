package webserver;

import webserver.http.HttpVersion;

public class WebServerConfig {

    public static final int PORT = 8080;
    public static final String HOST_NAME = "localhost";
    public static final String ENDPOINT = "http://" + HOST_NAME + ":" + PORT;
    public static final String BASE_PATH = "./webapp";
    public static final String ENTRY_FILE = "/index.html";
    public static final HttpVersion RESPONSE_HTTP_VERSION = new HttpVersion("HTTP/1.1");

}
