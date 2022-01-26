package webserver;

import java.io.*;
import java.net.Socket;

import controller.Controller;
import model.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.ViewResolver;
import http.HandlerAdapter;
import http.HandlerMapper;
import http.request.Request;
import http.response.Response;

/*
DispatchServlet 역할, 기존 RequestHandler
 */
public class DispatchServlet extends Thread {

    private static final Logger log = LoggerFactory.getLogger(DispatchServlet.class);

    private final Socket connection;
    private final HandlerMapper handlerMapper = HandlerMapper.getInstance();
    private final HandlerAdapter handlerAdapter = HandlerAdapter.getInstance();
    private final ViewResolver viewResolver = ViewResolver.getInstance();

    public DispatchServlet(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
             DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            Request request = new Request(br);
            request.read();

            Controller controller = handlerMapper.get(request.getMethod(), request.getUrl());
            ModelAndView mv = viewResolver.getView(checkController(controller, request));

            Response response = new Response(dos, mv);
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
}
