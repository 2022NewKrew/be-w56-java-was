package webserver;

import http.request.HttpRequest;
import http.response.HttpResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final HandlerMapping handlerMapping = HandlerMapping.getInstance();
    private static final HandlerExceptionResolver handlerExceptionResolver = HandlerExceptionResolver.getInstance();
    private static final TemplateView templateView = MyTemplateView.getInstance();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            try {
                HttpRequest httpRequest = IOUtils.readRequest(in);
                HttpResponse httpResponse = new HttpResponse();

                ModelAndView mv = handlerMapping.invokeHandlerMethod(httpRequest, httpResponse);
                mv.render(httpResponse, templateView);

                IOUtils.write(new DataOutputStream(out), httpResponse);
            } catch (IOException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                if (e instanceof InvocationTargetException) {
                    HttpResponse httpResponse = handlerExceptionResolver.resolveException(((InvocationTargetException) e).getTargetException());
                    IOUtils.write(new DataOutputStream(out), httpResponse);
                    return;
                }
                HttpResponse httpResponse = handlerExceptionResolver.resolveException(e);
                IOUtils.write(new DataOutputStream(out), httpResponse);
            }
        } catch (IOException e) {
            log.error("Failure while trying to get input/output stream\n message: " + e.getMessage());
        }
    }
}
