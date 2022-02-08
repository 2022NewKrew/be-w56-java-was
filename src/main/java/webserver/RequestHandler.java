package webserver;

import static util.IOUtils.*;
import static util.RequestParser.*;
import static util.ResponseParser.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.Method;
import http.Request;
import http.Response;

public class RequestHandler extends Thread {
    private static final String NEW_LINE = "\r\n";
    private static final String EMPTY = "";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                  connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String requestHeader = getRequestHeader(bufferedReader);
            String method = getMethod(requestHeader);
            String requestBody = getRequestBody(bufferedReader, requestHeader, method);

            Request request = getRequest(requestHeader, requestBody, method);

            Response response = new Response();
            String source = processRequest(request, response);

            byte[] responseBytes = responseToBytes(source, response);

            dataOutputStream.write(responseBytes, 0, responseBytes.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error("hi", e);
        }
    }

    private static String getRequestHeader(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = bufferedReader.readLine()).equals(EMPTY)) {
            stringBuilder.append(line);
            stringBuilder.append(NEW_LINE);
        }
        return stringBuilder.toString();
    }

    private static String getRequestBody(BufferedReader bufferedReader, String requestHeader, String method) throws IOException {
        if (method.equals(Method.GET.toString())) {
            return null;
        }
        Integer bodyLength = getRequestBodyLength(requestHeader);
        return readData(bufferedReader, bodyLength);
    }
}
