package http;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class ParametersTest {

    @Test
    public void createNullOrEmptyParameter() {
        assertThat(Parameters.create(null).getParameters())
                .isEqualTo(new HashMap<>());

        assertThat(Parameters.create("").getParameters())
                .isEqualTo(new HashMap<>());
    }

    @Test
    public void createSuccess() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("Id", "abcd");
        parameters.put("PW", "asdf1234");

        assertThat(Parameters.create("Id=abcd&PW=asdf1234").getParameters())
                .isEqualTo(parameters);
    }

    @Test
    public void createFailed() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("Id", "abcd");
        parameters.put("PW", "asdf1234");

        assertThat(Parameters.create("Id=abcd&PW=asdf1234&1234").getParameters())
                .isEqualTo(parameters);
    }
}
