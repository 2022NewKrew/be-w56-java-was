package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import com.google.common.collect.Maps;
import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log;
    private static final FrontController frontController;

    static {
        log = LoggerFactory.getLogger(RequestHandler.class);
        frontController = FrontController.getInstance();
    }

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader httpRequestHeaderReader = new BufferedReader(new InputStreamReader(in));

            String[] requestInfo = this.parseRequestInfo(httpRequestHeaderReader);

            String requestMethod = requestInfo[0];
            String requestPath = requestInfo[1];
            Map<String, String> queryParams = Maps.newHashMap();
            if(requestInfo.length == 3) {
                queryParams = HttpRequestUtils.parseQueryString(requestInfo[2]);
            }

            DataOutputStream dos = new DataOutputStream(out);
            frontController.dispatch(requestMethod, requestPath, queryParams, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String[] parseRequestInfo(BufferedReader httpRequestHeaderReader) throws IOException {
        String requestInfo = httpRequestHeaderReader.readLine();

        String[] requestInfoTokens = requestInfo.split(" ");

        String requestMethod = requestInfoTokens[0];
        String[] requestUrl = requestInfoTokens[1].split("\\?");

        if(requestUrl.length == 1) {
            return new String[] { requestMethod, requestUrl[0] };
        }

        return new String[] { requestMethod, requestUrl[0], requestUrl[1] };
    }
}
