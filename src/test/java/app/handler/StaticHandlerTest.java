package app.handler;

import lib.was.http.Locator;
import lib.was.http.Request;
import lib.was.http.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class StaticHandlerTest {

    private StaticHandler subject;

    @BeforeEach
    void setUp() {
        subject = new StaticHandler();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/index.html", "/images/80-text.png"})
    void get(String path) throws IOException {
        Locator locator = Locator.parse(path);
        Request request = Request.newBuilder().locator(locator).build();

        Response result = subject.get(request);

        byte[] expected = Files.readAllBytes(Path.of("./webapp" + path));
        assertArrayEquals(expected, result.getBody());
    }
}
