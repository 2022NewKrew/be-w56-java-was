package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BaseException;
import webserver.infra.Router;
import webserver.infra.ViewResolver;
import webserver.model.HttpRequest;
import webserver.model.HttpResponse;
import webserver.model.ModelAndView;
import webserver.view.ErrorView;
import webserver.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Router router = Router.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                OutputStream out = connection.getOutputStream();
        ) {
            HttpRequest request = new HttpRequest(br);
            HttpResponse response = new HttpResponse(out);

            handleRequest(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
        try {
            ModelAndView modelAndView = router.route(request, response);
            View view = viewResolver.resolve(modelAndView);
            view.render(response);
        } catch (BaseException e) {
            ErrorView errorView = new ErrorView(e);
            errorView.render(response);
        }
    }

}
