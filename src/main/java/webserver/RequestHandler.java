package webserver;

import mapper.ResponseSendDataModel;
import mapper.UrlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class RequestHandler {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private final UrlMapper urlMapper = new UrlMapper();

    public void sendResponseMessage(Socket socket){
        log.debug("New Client Connect! Connected IP : {}, Port : {}", socket.getInetAddress(),
                socket.getPort());

        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            BufferedReader br = new BufferedReader( new InputStreamReader(in, StandardCharsets.UTF_8) );

            HttpRequest httpRequest = HttpRequest.makeHttpRequest(br);

            ResponseSendDataModel result = urlMapper.mappingResult(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);

            HttpResponse httpResponse = HttpResponse.makeHttpResponse(result);
            httpResponse.writeHttpResponse(dos);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
