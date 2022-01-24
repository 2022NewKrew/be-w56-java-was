package webserver;

import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import http.HttpResponseRenderer;
import http.impl.HttpFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerErrorException;
import webserver.exception.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestHandlerInternal {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerInternal.class);

    public void run(InputStream in, OutputStream out, HttpProcessor httpProcessor) throws IOException {
        try {
            HttpRequest httpRequest = parseHttpRequest(in);
            HttpResponse response = httpProcessor.process(httpRequest);
            ByteArrayOutputStream bos = renderOutputStream(response);
            printByteArrayOutputStreamToOutputStream(bos, out);
        }  catch (InternalServerErrorException e) {
            log.error("서버 예외 : {} ", e.getMessage());
        } catch (ResourceNotFoundException e) {
            log.error("자원 없음 예외 : {} ", e.getMessage());
        } catch (BadRequestException e) {
            log.error("잘못된 요청 예외 : {} ", e.getMessage());
        } catch (IOException e) {
            throw e;
        }
    }

    private HttpRequest parseHttpRequest(InputStream in) {
        HttpRequestParser parser = HttpFactory.getHttpRequestParser();
        return parser.parse(in);
    }

    private ByteArrayOutputStream renderOutputStream(HttpResponse httpResponse) {
        HttpResponseRenderer httpResponseRenderer = HttpFactory.getHttpResponseRenderer();
        return httpResponseRenderer.render(httpResponse);
    }

    private void printByteArrayOutputStreamToOutputStream(ByteArrayOutputStream bos, OutputStream os) throws IOException {
        byte[] response = bos.toByteArray();
        os.write(response, 0, response.length);
    }
}
