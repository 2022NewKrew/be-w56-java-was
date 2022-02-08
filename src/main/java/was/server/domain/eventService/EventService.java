package was.server.domain.eventService;

@FunctionalInterface
public interface EventService {
    byte[] doService(byte[] requestBytes);
}
