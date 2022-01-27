package util;

import http.HttpResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseUtilsTest {

    @Test
    void contentTypeFromPath() {
        String responseDataPath = "/index.html";
        String contentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/html");

        responseDataPath = "/css/bootstrap.min.css";
        contentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/css");

        responseDataPath = "/js/jquery-2.2.0.min.js";
        contentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("application/javascript");

        responseDataPath = "/favicon.ico";
        contentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("image/x-icon");

        responseDataPath = "/";
        contentType = HttpResponseUtils.contentTypeFromPath(responseDataPath);
        assertThat(contentType).isEqualTo("text/html");
    }
}
