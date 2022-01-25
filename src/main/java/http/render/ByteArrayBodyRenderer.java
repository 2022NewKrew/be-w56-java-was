package http.render;

import java.io.ByteArrayOutputStream;

public class ByteArrayBodyRenderer implements ResponseBodyRenderer {

    @Override
    public boolean supports(Object body) {
        return body != null && body.getClass().isAssignableFrom(byte[].class);
    }

    @Override
    public ByteArrayOutputStream render(Object body) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = (byte[]) body;
        byteArrayOutputStream.write(bytes, 0, bytes.length);
        return byteArrayOutputStream;
    }
}
