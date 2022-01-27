package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HttpResponseTest {

    @Test
    void contentTypeFromPath() {
        final HttpResponse httpResponse = new HttpResponse();
        String responseDataPath = "/index.html";
        String contentType = httpResponse.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/html");

        responseDataPath = "/css/bootstrap.min.css";
        contentType = httpResponse.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/css");

        responseDataPath = "/js/jquery-2.2.0.min.js";
        contentType = httpResponse.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("application/javascript");

        responseDataPath = "/favicon.ico";
        contentType = httpResponse.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("image/x-icon");

        responseDataPath = "/";
        contentType = httpResponse.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/html");

    }
}
