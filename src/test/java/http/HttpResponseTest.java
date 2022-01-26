package http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class HttpResponseTest {

    @Test
    void contentTypeFromUrl() {
        final HttpResponse httpResponse = new HttpResponse();
        String url = "/index.html";
        String contentType = httpResponse.contentTypeFromUrl(url);
        assertThat(contentType).isEqualTo("text/html");

        url = "/css/bootstrap.min.css";
        contentType = httpResponse.contentTypeFromUrl(url);
        assertThat(contentType).isEqualTo("text/css");

        url = "/js/jquery-2.2.0.min.js";
        contentType = httpResponse.contentTypeFromUrl(url);
        assertThat(contentType).isEqualTo("application/javascript");

        url = "/favicon.ico";
        contentType = httpResponse.contentTypeFromUrl(url);
        assertThat(contentType).isEqualTo("image/x-icon");

        url = "/";
        contentType = httpResponse.contentTypeFromUrl(url);
        assertThat(contentType).isEqualTo("application/octet-stream");

    }
}
