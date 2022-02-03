package was.domain.eventLoop;

@FunctionalInterface
public interface EventService {
    byte[] doService(byte[] requestBytes);
}
