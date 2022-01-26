package webserver;

import controller.UserController;
import util.HttpRequestUtils;

import java.util.Map;

public class RequestMethodManager {

	public RequestMethodManager(String method, String path, Map<String, String> params) {
		method = method.toUpperCase();
		String rootPath = HttpRequestUtils.parseUrlRootPath(path);
		switch (rootPath) {
			case "/user":
				UserController userController = new UserController(method, path, params);
				break;
			case "/index.html":
				break;
		}
	}

}
