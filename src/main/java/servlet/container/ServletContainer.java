package servlet.container;

import http.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.ServletRequest;
import servlet.ServletResponse;
import servlet.filter.LoginFilter;
import servlet.filter.ServletFilter;
import servlet.view.View;
import servlet.view.ViewResolver;
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
    private final ServletFilter filter;

    private ServletContainer(Map<MappingKey, Servlet> container, ServletFilter filter) {
        logger.debug("Create ServletContainer");
        this.container = container;
        this.filter = filter;
    }

    public static ServletContainer getInstance() {
        return instance;
    }

    static ServletContainer create() {
        logger.debug("Initialize ServletContainer");
        List<Class<?>> controllers = new ArrayList<>(Arrays.asList(UserController.class));
        Map<MappingKey, Servlet> container = controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .collect(Collectors.toMap(MappingKey::create, Servlet::create));
        return new ServletContainer(container, LoginFilter.create(Arrays.asList("/user/list")));
    }

    public ResponseMessage process(ServletRequest request, ServletResponse response) {
        Servlet servlet = container.get(request.createMappingKey());
        filter.process(request, response, servlet);
        View view = ViewResolver.findView(response);
        return view.render();
    }

    public void destroy() {
        logger.debug("Destroy ServletContainer");
    }
}
