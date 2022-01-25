package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread {

	private static final Logger log = LoggerFactory.getLogger(ConnectionThread.class);

	private final Socket connection;

	public ConnectionThread(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			RequestHandler reqHandler = new RequestHandler(in);
			ResponseHandler resHandler = new ResponseHandler(out);
			resHandler.sendRes(reqHandler.getPath(), reqHandler.isRedirect(), reqHandler.getDestinationUrl());

		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
