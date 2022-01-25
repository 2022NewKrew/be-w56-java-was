package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestHandler {

	private static final Logger log = LoggerFactory.getLogger(ConnectionThread.class);
	private static String method;
	private static String path;
	private static Map<String, String> params;
	private static boolean isRedirect;
	private static String destinationUrl;

	public RequestHandler() {
	}

	public RequestHandler(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		readHeader(br);
		readBody(br);
		RequestMethodManager requestMethodManager = new RequestMethodManager(method, path, params);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		RequestHandler.path = path;
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean redirect) {
		isRedirect = redirect;
	}

	public String getDestinationUrl() {
		return destinationUrl;
	}

	public void setDestinationUrl(String destinationUrl) {
		RequestHandler.destinationUrl = destinationUrl;
	}

	public void readHeader(BufferedReader br) throws IOException {
		String line = br.readLine();
		parseFirstLine(line);
		log.debug("request line : " + line);

		while (line != null && !line.equals("")) {
			line = br.readLine();
			log.debug("header : " + line);
		}
	}

	public void parseFirstLine(String firstLine) {
		Map<String, String> line = HttpRequestUtils.parseReqFirstLine(firstLine);
		method = line.get("method");
		String url = line.get("url");
		path = HttpRequestUtils.parseUrl(url).get("path");
		String parameters = HttpRequestUtils.parseUrl(url).get("parameters");
		if (parameters != null) params = HttpRequestUtils.parseQueryString(parameters);
	}

	public void readBody(BufferedReader br) throws IOException {
		if (hasBody()) {
			String line = br.readLine();
			if (line != null) {
				log.debug("request body : " + line);
				params = HttpRequestUtils.parseQueryString(line);
			}
			while (line != null && !line.equals("")) {
				line = br.readLine();
				log.debug("body : " + line);
			}
		}
	}

	public boolean hasBody() {
		return method == "POST" || method == "PUT";
	}

}
