package bin.jayden.webserver;

import bin.jayden.http.HttpStatusCode;
import bin.jayden.http.MyHttpRequest;
import bin.jayden.http.MyHttpResponse;
import bin.jayden.http.MyHttpResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final byte[] SERVER_ERROR_MESSAGE = "서버에서 에러가 발생했습니다.".getBytes();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            MyHttpResponse response;
            try {
                MyHttpRequest request = new MyHttpRequest.Builder().build(in);
                //printHeaders(request.getHeader());
                response = Router.routing(request);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                response = new MyHttpResponse.Builder()
                        .setBody(SERVER_ERROR_MESSAGE)
                        .setStatusCode(HttpStatusCode.STATUS_CODE_500)
                        .build();
            }
            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(OutputStream out, MyHttpResponse response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] responseBytes = response.toBytes();
        dos.write(responseBytes, 0, responseBytes.length);
        dos.flush();
    }

    private void printHeaders(MyHttpResponseHeader header) {
        for (Map.Entry<String, String> entry : header.getEntrySet()) {
            log.info("Header : [{} : {}]", entry.getKey(), entry.getValue());
        }
    }
}
