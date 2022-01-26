package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import controller.Controller;
import controller.ControllerDeprecated;
import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewResolver;
import http.HandlerAdapter;
import http.HandlerMapper;
import http.HttpStatusCode;
import http.ResponseData;
import http.request.Request;
import http.response.Response;

/*
DispatchServlet 역할
 */
public class RequestHandler extends Thread {
    private static final String ROOT_DIRECTORY = "./webapp";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HandlerMapper handlerMapper = HandlerMapper.getInstance();
    private final HandlerAdapter handlerAdapter = HandlerAdapter.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                OutputStream out = connection.getOutputStream()) {
            Request request = new Request(br);
            request.read();

            Controller controller = handlerMapper.get(request.getMethod(), request.getUrl());
            ModelAndView mv = checkController(controller, request);
            String view = viewResolver.getView(mv);

            Response response = new Response(out, checkDirectory(request));
            response.write();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private ModelAndView checkController(Controller controller, Request request){
        if(controller == null){
            return new ModelAndView(request.getUrl());
        }
        return handlerAdapter.handleController(controller, request);
    }

    private ResponseData checkDirectory(Request request){
        if(Files.isRegularFile(Path.of(ROOT_DIRECTORY+ request.getUrl()))){
            log.info("File exist");
            return new ResponseData(HttpStatusCode.SUCCESS, request.getUrl());
        }
        log.info("URL mapping");
        return ControllerDeprecated.proceed(request);
    }
}
