package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ConnectionThread;
import webserver.RequestHandler;

import java.util.Map;

public class UserController {

	private static final Logger log = LoggerFactory.getLogger(ConnectionThread.class);
	private static final RequestHandler reqHandler = new RequestHandler();

	public UserController(String method, String path, Map<String, String> params) {
		analyzeUrl(method, path, params);
	}

	public void analyzeUrl(String method, String path, Map<String, String> params) {
		log.debug("UserController :" + " [method: " + method + "] [path: " + path + "] [params: " + params.toString());
		switch (path) {
			case "/user/create":
				save(params);
				break;
		}
	}

	public void save(Map<String, String> params) {
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.convertValue(params, User.class);
		reqHandler.setRedirect(true);
		reqHandler.setDestinationUrl("/user/login.html");
	}

}
