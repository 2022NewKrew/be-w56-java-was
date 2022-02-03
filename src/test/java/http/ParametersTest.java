package http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParametersTest {

    @Test
    public void createNullOrEmptyParameter() {
        assertThat(Parameters.create(Optional.empty()).getParameters())
                .isEqualTo(new HashMap<>());

        assertThat(Parameters.create(Optional.of("")).getParameters())
                .isEqualTo(new HashMap<>());
    }

    @Test
    public void createSuccess() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("Id", "abcd");
        parameters.put("PW", "asdf1234");

        assertThat(Parameters.create(Optional.of("Id=abcd&PW=asdf1234")).getParameters())
                .isEqualTo(parameters);
    }

    @Test
    public void createFailed() {
        assertThatThrownBy(() -> Parameters.create(Optional.of("Id=abcd&PW=asdf1234&1234")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
