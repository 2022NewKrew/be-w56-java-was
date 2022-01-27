package com.leoserver.webserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leoserver.webserver.handler.RequestHandler;

public class WebServer {
  private static final Logger log = LoggerFactory.getLogger(WebServer.class);
  private static final int DEFAULT_PORT = 8080;

  public static void main(String args[]) throws Exception {
    int port = 0;
    if (args == null || args.length == 0) {
      port = DEFAULT_PORT;
    } else {
      port = Integer.parseInt(args[0]);
    }

    ApplicationContext context = new ApplicationContext();

    try (ServerSocket listenSocket = new ServerSocket(port)) {
      log.info("Web Application Server started {} port.", port);

      // 클라이언트가 연결될때까지 대기한다.
      Socket connection;
      while ((connection = listenSocket.accept()) != null) {

        log.debug("왜 커넥션이 2개 생기지? : {}", connection);

        RequestHandler requestHandler = new RequestHandler(
            connection,
            context
        );
        requestHandler.start();
      }
    }
  }
}
