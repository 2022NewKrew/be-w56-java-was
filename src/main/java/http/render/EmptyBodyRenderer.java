package http.render;

import java.io.ByteArrayOutputStream;

public class EmptyBodyRenderer implements ResponseBodyRenderer{

    @Override
    public boolean supports(Object body) {
        return body == null;
    }

    @Override
    public ByteArrayOutputStream render(Object body) {
        return new ByteArrayOutputStream();
    }
}
