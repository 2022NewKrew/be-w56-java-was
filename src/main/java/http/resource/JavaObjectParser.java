package http.resource;

public interface JavaObjectParser {

    String getType();

    String parse(Object object);
}
