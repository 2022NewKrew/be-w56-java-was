package infrastructure.model;

public interface HttpBody {
    byte[] toByteStream();
    int length();
}
