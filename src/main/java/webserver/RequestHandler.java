package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import controller.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = createHttpRequest(br);
            log.debug(httpRequest.toMessage());

            HttpResponse httpResponse = createHttpResponse();

            FrontController.getResponse(httpRequest, httpResponse);
            response(dos, httpResponse);

            br.close();
            dos.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static HttpRequest createHttpRequest(BufferedReader br) throws IOException {

        String[] tokens = br.readLine().split(" ");
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            headers.put(line.split(":")[0].trim(), line.split(":")[1].trim());
            line = br.readLine();
        }
        return new HttpRequest(tokens[0], tokens[1], tokens[2], headers, "");
    }

    public static HttpResponse createHttpResponse(){
        return new HttpResponse();
    }

    private void response(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.toHeader());
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
