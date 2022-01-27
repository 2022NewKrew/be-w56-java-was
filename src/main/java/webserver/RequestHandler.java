package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Function;

public class RequestHandler extends Thread {
    private static final ControlMappingTable mappingTable = new ControlMappingTable();
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            log.debug("Request : {}", request);

            DataOutputStream dos = new DataOutputStream(out);

            // TODO 다양한 exception에 반응 할 수 있도록 Custom Exception 만들기
            Function<Request, String> foundMethod = mappingTable.findMethod(request.getType(), request.getUri());
            String ret = foundMethod.apply(request);
            HttpResponseUtils.make200Response(dos, ret);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
