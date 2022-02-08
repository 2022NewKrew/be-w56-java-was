package webserver;

import exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.infra.Router;
import webserver.infra.ViewResolver;
import webserver.model.Model;
import webserver.model.ModelAndView;
import webserver.view.ErrorView;
import webserver.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

@Slf4j
public class RequestHandler extends Thread {

    private static final Router router = new Router();
    private static final ViewResolver viewResolver = new ViewResolver();

    private final Socket connection;

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
            view.render(response, modelAndView.getModel());
        } catch (BaseException e) {
            ErrorView errorView = new ErrorView(e);
            errorView.render(response, new Model());
        }
    }

}
