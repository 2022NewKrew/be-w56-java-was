package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import springmvc.FrontController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out)) {

            HttpRequest httpRequest = createHttpRequest(br);
            log.debug(httpRequest.toMessage());
            HttpResponse httpResponse = createEmptyHttpResponse();

            FrontController.doService(httpRequest, httpResponse);

            response(dos, httpResponse);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static HttpRequest createHttpRequest(BufferedReader br) throws IOException {

        String[] tokens = br.readLine().split(" ");
        Map<String, String> headers = IOUtils.readHeader(br);
        String body = headers.containsKey("Content-Length") ?
                IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))) : "";
        return new HttpRequest(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2], headers, body);
    }

    private static HttpResponse createEmptyHttpResponse(){
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
