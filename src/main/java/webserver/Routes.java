package webserver;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import controller.BaseController;
import controller.StaticController;

public class Routes {

    private final Table<HttpMethod, String, BaseController> routes;
    private final StaticController staticController = new StaticController();

    public Routes() {
        routes = HashBasedTable.create();
        setRoutes();
    }

    private void setRoutes() {
        // Request Mapping to Controller
    }

    public BaseController getController(HttpMethod httpMethod, String url) {
        if (routes.contains(httpMethod, url)) {
            return routes.get(httpMethod, url);
        }
        return staticController;
    }

}
