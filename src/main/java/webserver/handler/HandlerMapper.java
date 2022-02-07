package webserver.handler;

import webserver.exception.NoMatchingHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.util.Map;

/**
 * A mapper responsible for finding proper handler for given request
 * <p>
 * Populated via handler scanning on server booting.
 * <p>
 * The returned handler is a {@link WrappedHandler}
 * which has the unified {@link WrappedHandler#handle(HttpRequest, HttpResponse) handle} method
 * to invoke the actual handler.
 */
public class HandlerMapper {
    private final Map<HandlerMatcher, WrappedHandler> handlerMappings;

    // Should creation of HandlerMapper be its own concern?
    // HandlerMapper creation is a rather complicated procedure.
    // 1. Scan all classes through reflection and filter by @Controller annotation
    // 2. Scan their methods and generate appropriate HandlerMatcher and WrappedHandler for each method
    // 3. Finally put these pair on LinkedHashMap
    public HandlerMapper(Map<HandlerMatcher, WrappedHandler> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    // https://stackoverflow.com/questions/2923856/is-the-order-guaranteed-for-the-return-of-keys-and-values-from-a-linkedhashmap-o/2924143#2924143
    public WrappedHandler findMatchingHandler(HttpRequest httpRequest) {
        return handlerMappings.entrySet().stream().filter(
                handlerMapping -> handlerMapping.getKey().match(httpRequest)).findFirst().orElseThrow(
                NoMatchingHandler::new).getValue();
    }
}
