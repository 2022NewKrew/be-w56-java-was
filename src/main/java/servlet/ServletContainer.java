package servlet;

import http.*;
import web.controller.UserController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ServletContainer {
    private static final ServletContainer instance = ServletContainer.create();
    private final Map<MappingKey, Servlet> container;

    //private static final Logger logger = LoggerFactory.getLogger(ServletContainer.class);

    private ServletContainer(Map<MappingKey, Servlet> container) {
        //logger.debug("Create ServletContainer");
        this.container = container;
    }

    public static ServletContainer getInstance() {
        return instance;
    }

    private static ServletContainer create() {
        //logger.debug("Initialize ServletContainer");
        List<Class<?>> controllers = new ArrayList<>(Arrays.asList(UserController.class));
        return new ServletContainer(controllers.stream()
                .flatMap(controller -> Arrays.stream(controller.getDeclaredMethods()))
                .collect(Collectors.toMap(MappingKey::create, Servlet::create)));
    }

    public ResponseMessage process(RequestMessage request) {
        // TODO 예외처리
        String key = request.getStatusLine().getMethod().toString() + request.getStatusLine().getRequestTarget().getPath().getValue();
        Servlet servlet = container.get(new MappingKey(key));
        try {
            String path = servlet.service(request);
            byte[] bytes = Files.readAllBytes(new File("./webapp" + path).toPath());
            Body body = new Body(bytes);
            StatusLine statusLine = new StatusLine(HttpVersion.V_1_1, HttpStatus.OK);
            Headers responseHeaders = body.createResponseHeader();
            return new ResponseMessage(statusLine, responseHeaders, body);
        } catch (InvocationTargetException | IllegalAccessException | IOException e) {
            e.printStackTrace();
            Body body = new Body();
            StatusLine statusLine = new StatusLine(HttpVersion.V_1_1, HttpStatus.NOT_FOUND);
            Headers responseHeaders = body.createResponseHeader();
            return new ResponseMessage(statusLine, responseHeaders, body);
        }
    }

    public void destroy() {
        // 모든 서블릿을 내린다.
        //logger.debug("Destroy ServletContainer");
    }

    // TODO 모든 서블릿 클래스 로드, 초기화, 호출, 소멸 라이프 사이클 관리
}
