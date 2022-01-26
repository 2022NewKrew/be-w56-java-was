package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springmvc.frontcontroller.FrontController;
import webserver.http.request.CustomHttpRequest;
import webserver.http.request.HttpRequestHandler;
import webserver.http.response.CustomHttpResponse;
import webserver.http.response.HttpResponseHandler;

import javax.management.ServiceNotFoundException;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    //front controller
    private final FrontController frontController;

    //http
    private HttpRequestHandler httpRequestHandler;
    private HttpResponseHandler httpResponseHandler;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        httpRequestHandler = new HttpRequestHandler();
        httpResponseHandler = new HttpResponseHandler();
        frontController = new FrontController();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            httpRequestHandler.parseRequest(br);

            CustomHttpRequest httpRequest = httpRequestHandler.getHttpRequest();
            CustomHttpResponse httpResponse = httpResponseHandler.getHttpResponse();
            String viewPath = null;

            if(httpRequestHandler.isStaticResourceRequest()) {
                viewPath = "./webapp" + httpRequest.getRequestURI();
            } else {
                viewPath = frontController.service(httpRequest, httpResponse);
            }

            log.debug("view path : {}", viewPath);
            httpResponse.setBody(Files.readAllBytes(new File(viewPath).toPath()));
            httpResponseHandler.writeResponseHeader(httpRequest);
            writeWithOutPutStream(new DataOutputStream(out), httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (ServiceNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private void writeWithOutPutStream(DataOutputStream dos, CustomHttpResponse response) throws IOException {
        log.debug("header content : {}", response.getResponseHeaderContent());
        dos.writeBytes(response.getResponseHeaderContent());
        dos.write(response.getBody().getBodyContent());

        dos.flush();
        dos.close();
    }
}
