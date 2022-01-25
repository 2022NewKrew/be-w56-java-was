package webserver.model;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import controller.BaseController;
import controller.StaticResourceController;
import controller.user.SignupController;

public class Routes {

    private final Table<HttpMethod, String, BaseController> routes;
    private final StaticResourceController staticResourceController = new StaticResourceController();

    public Routes() {
        routes = HashBasedTable.create();
        setRoutes();
    }

    private void setRoutes() {
        // Request Mapping to Controller
        routes.put(HttpMethod.GET, "/user/create", new SignupController());
    }

    public BaseController getController(HttpMethod httpMethod, String url) {
        if (routes.contains(httpMethod, url)) {
            return routes.get(httpMethod, url);
        }
        if (httpMethod.equals(HttpMethod.GET)) {
            return staticResourceController;
        }
        throw new IllegalArgumentException(String.format("지원하지 않는 요청입니다. %s %s", httpMethod.name(), url));
    }

}
