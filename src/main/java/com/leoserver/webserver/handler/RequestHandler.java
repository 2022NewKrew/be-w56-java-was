package com.leoserver.webserver.handler;

import com.leoserver.webserver.ApplicationContext;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leoserver.webserver.http.HttpParser;
import com.leoserver.webserver.http.KakaoHttpRequest;
import com.leoserver.webserver.http.Uri;

public class RequestHandler extends Thread {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

  private final Socket connection;
  private final StaticResourceHandler staticResourceHandler;
  private final ServletHandler servletHandler;
  private final ApplicationContext applicationContext;

  public RequestHandler(Socket connectionSocket, ApplicationContext applicationContext) {
    this.connection = connectionSocket;
    this.applicationContext = applicationContext;
    this.staticResourceHandler = applicationContext.getStaticResourceHandler();
    this.servletHandler = applicationContext.getServletHandler();
  }

  public void run() {

    log.debug("New Client Connect! Connected IP : {}, Port : {}, Connection : {}", connection.getInetAddress(),
        connection.getPort(), connection);

    try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

      // Request
      KakaoHttpRequest request = new HttpParser(in).toRequest();
      byte[] response;
      log.debug(request.toString());

      if(servletHandler.hasMappedMethod(request)) {
        response = servletHandler.handle(request);
      }else {
        response = staticResourceHandler.handle(request);
      }

      // Response
      log.debug("response by : {}", request);
      DataOutputStream dos = new DataOutputStream(out);
      dos.write(response);
      dos.flush();

    } catch (Exception e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }


  private boolean isStaticRequest(KakaoHttpRequest request) {
    Uri uri = request.getUri();
    return uri.hasExtension();
  }

}
