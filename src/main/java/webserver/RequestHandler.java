package webserver;

import controller.Controller;
import http.request.HttpRequest;
import http.response.HttpResponse;
import http.response.HttpResponseFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest request = new HttpRequest(inputStreamToStrings(in));
            Map<String, String> model = new HashMap<>();
            Controller controller = ControllerType.getControllerType(request.getUrl());
            Map<String, String> result = controller.run(request, model);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = HttpResponseFactory.getHttpResponse(result, model, dos);
            response.sendResponse();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String inputStreamToStrings(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while (!(line = bufferedReader.readLine()).equals("")) {
            result.append(line).append(Constant.lineBreak);
        }
        return result.toString();
    }
}
