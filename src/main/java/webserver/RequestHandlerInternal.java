package webserver;

import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import http.render.HttpResponseRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerErrorException;
import webserver.exception.ResourceNotFoundException;
import webserver.processor.HttpProcessor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RequestHandlerInternal {

    private static final Logger log = LoggerFactory.getLogger(RequestHandlerInternal.class);

    public void run(InputStream in, OutputStream out) throws IOException {
        HttpProcessor processor = HttpFactory.httpProcessor();
        try {
            HttpRequest httpRequest = parseHttpRequest(in);
            HttpResponse response = processor.process(httpRequest);
            ByteArrayOutputStream bos = renderOutputStream(response);
            bos.writeTo(out);
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
        HttpRequestParser parser = HttpFactory.httpRequestParser();
        return parser.parse(in);
    }

    private ByteArrayOutputStream renderOutputStream(HttpResponse httpResponse) {
        HttpResponseRenderer httpResponseRenderer = HttpFactory.httpResponseRenderer();
        return httpResponseRenderer.render(httpResponse);
    }
}
