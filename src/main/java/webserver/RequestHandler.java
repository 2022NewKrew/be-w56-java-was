package webserver;

import com.google.common.io.Files;
import controller.Controller;
import http.request.HttpRequest;
import http.response.ContentType;
import http.response.HttpResponse;
import http.response.StatusCode;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Constant;
import view.ViewMaker;

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
            String result = controller.run(request, model);
            byte[] body = ViewMaker.getView(result, model);
            String resultExtension = getExtension(result);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos, StatusCode.OK, ContentType.getContentType(resultExtension), body);
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
        while(!(line = bufferedReader.readLine()).equals("")){
            log.info(line);
            result.append(line).append(Constant.lineBreak);
        }
        return result.toString();
    }

    private String getExtension(String result) {
        List<String> splitResult =  List.of(result.split("\\."));
        int length = splitResult.size();
        return splitResult.get(length - 1);
    }
}
