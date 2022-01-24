package http;

import java.io.InputStream;

public interface HttpRequestParser {
    HttpRequest parse(InputStream in);
}
