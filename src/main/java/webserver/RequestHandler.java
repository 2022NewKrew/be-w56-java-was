package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.GetRequestProcess;
import request.PostRequestProcess;
import request.RequestProcess;
import util.ResponseData;

public class RequestHandler extends Thread {
    public static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            RequestProcess requestProcess = getRequestProcessByMethod(br);
            requestProcess.process();
            ResponseData result = requestProcess.getResponseData();

            responseHeader(dos, result.getHeader());
            responseBody(dos, result.getBody());
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }
    }

    private RequestProcess getRequestProcessByMethod(BufferedReader br) throws NoSuchMethodException, IOException {
        String[] tokens = br.readLine().split(" ");
        String method = tokens[0];
        String url = tokens[1];
        log.debug(method + " " + url);
        if ("GET".equals(method)) return new GetRequestProcess(url);
        if ("POST".equals(method)) return new PostRequestProcess(url, br);

        throw new NoSuchMethodException();
    }

    private void responseHeader(DataOutputStream dos, String header) throws IOException {
        dos.writeBytes(header);
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
