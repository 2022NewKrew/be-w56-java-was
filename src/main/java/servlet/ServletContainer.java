package servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.controller.UserController;

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

    public void process(ServletRequest request, ServletResponse response) {
        Servlet servlet = container.get(request.createMappingKey());
        servlet.service(request, response);

        View view = ViewResolver.findView(response);
        view.render();
        view.createResponse();
    }

    public void destroy() {
        logger.debug("Destroy ServletContainer");
    }
}
