package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class ResponseHandler {

	private static final Logger log = LoggerFactory.getLogger(ConnectionThread.class);
	private final DataOutputStream dos;
	private byte[] body;

	public ResponseHandler(OutputStream out) {
		dos = new DataOutputStream(out);
	}

	public void sendRes(String path, boolean isRedirect, String destinationUrl) throws IOException {
		if (isRedirect) {
			write302Header(destinationUrl);
			send();
			return;
		}
		writeBody(path);
		write200Header();
		send();
	}

	public void write200Header() throws IOException {
		int lengthOfBodyContent = body.length;
		dos.writeBytes("HTTP/1.1 200 OK \r\n");
		// dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
		dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
		dos.writeBytes("\r\n");
	}

	public void write302Header(String destination) throws IOException {
		final String domain = "http://localhost:8080";
		dos.writeBytes("HTTP/1.1 302 Found \r\n");
		dos.writeBytes("Location: " + domain + destination);
		dos.writeBytes("\r\n");
	}

	public void writeBody(String path) throws IOException {
		body = Files.readAllBytes(new File("./webapp" + path).toPath());
		log.debug("response file path : /webapp" + path);
		dos.write(body, 0, body.length);
	}

	public void send() throws IOException {
		dos.flush();
	}

}
