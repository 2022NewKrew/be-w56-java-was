package http;

import java.io.ByteArrayOutputStream;

public interface HttpResponseRenderer {
    ByteArrayOutputStream render(HttpResponse response);
}
