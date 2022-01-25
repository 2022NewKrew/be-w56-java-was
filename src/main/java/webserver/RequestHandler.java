package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.security.NoSuchProviderException;

import controller.Controller;
import controller.ControllerCommander;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ModelAndView;
import view.ViewResolver;
import webserver.config.WebConst;

import static util.HttpResponseUtils.response200Header;
import static util.HttpResponseUtils.responseBody;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream res = new DataOutputStream(out);
            HttpRequest httpRequest = HttpRequest.from(in);
            createResponse(httpRequest, res);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createResponse(HttpRequest request, DataOutputStream dos) throws IOException {
        log.info("[REQUEST URI] - " + request.getRequestUri());
        try {
            Controller controller = ControllerCommander.findController(request);
            ModelAndView modelAndView = controller.execute(request, dos);

            //redirect 에 따라서 조절 해야함. 다 옮기기
            byte[] body = ViewResolver.render(modelAndView.getViewName());

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void printReq(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line = bufferedReader.readLine();
        while(!"".equals(line)) {
            System.out.println(line);
            line = bufferedReader.readLine();
        }
    }
}
