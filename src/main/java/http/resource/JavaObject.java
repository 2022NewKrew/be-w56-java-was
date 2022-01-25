package http.resource;

import java.nio.charset.StandardCharsets;

public class JavaObject implements Resource{

    private Object object;
    private JavaObjectParser parser;

    public JavaObject(Object object, JavaObjectParser javaObjectParser) {
        this.object = object;
        this.parser = javaObjectParser;
    }

    @Override
    public String getType() {
        return parser.getType();
    }

    @Override
    public byte[] getContent() {
        return parser.parse(object).getBytes(StandardCharsets.UTF_8);
    }
}
