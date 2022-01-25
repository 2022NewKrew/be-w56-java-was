package http.render;

import java.io.ByteArrayOutputStream;

public interface ResponseBodyRenderer {
    boolean supports(Object body);
    ByteArrayOutputStream render(Object body);
}
