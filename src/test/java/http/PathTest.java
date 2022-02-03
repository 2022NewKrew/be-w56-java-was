package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {

    @Test
    void createNullPath_ThrowException() {
        assertThatThrownBy(() -> new Path(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createEmptyPath_ThrowException() {
        assertThatThrownBy(() -> new Path(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void startWithNoPrefix_ThrowException() {
        assertThatThrownBy(() -> new Path("abc"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void startWithPrefix() {
        new Path("/abc");
    }
}
