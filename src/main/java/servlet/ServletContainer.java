package servlet;

import http.Cookie;
import http.HttpStatus;
import http.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.controller.UserController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServletContainer {
    private static final Logger logger = LoggerFactory.getLogger(ServletContainer.class);
    private static final ServletContainer instance = ServletContainer.create();
    private final Map<MappingKey, Servlet> container;

    private ServletContainer(Map<MappingKey, Servlet> container) {
        logger.debug("Create ServletContainer");
        this.container = container;
    }

    public static ServletContainer getInstance() {
        return instance;
    }

    private static ServletContainer create() {
        logger.debug("Initialize ServletContainer");
        List<Class<?>> controllers = new ArrayList<>(Arrays.asList(UserController.class));
        return new ServletContainer(controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .collect(Collectors.toMap(MappingKey::create, Servlet::create)));
    }

    public ResponseMessage process(ServletRequest request) {
        MappingKey key = request.createMappingKey();
        Servlet servlet = container.get(key);
        try {
            Cookie cookie = Cookie.parse(null);
            String path = servlet.service(request, cookie);
            if (path.contains("redirect")) {
                String url = "http://localhost:8080" + path.substring(path.indexOf(":") + 1);
                return ResponseMessage.create(HttpStatus.FOUND, url, cookie);
            }
            byte[] bytes = Files.readAllBytes(new File("./webapp" + path).toPath());
            return ResponseMessage.create(HttpStatus.OK, bytes);
        } catch (InvocationTargetException | IllegalAccessException | IOException e) {
            logger.error("ResponseMessage process : {}", e.toString());
            return ResponseMessage.create(HttpStatus.NOT_FOUND, new byte[]{});
        }
    }

    public void destroy() {
        logger.debug("Destroy ServletContainer");
    }
}
