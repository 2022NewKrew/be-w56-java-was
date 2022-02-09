package webserver.filter;

import webserver.http.request.HttpRequest;

import java.util.ArrayList;
import java.util.List;

public class FilterRegister {
    private static List<Filter> filters = new ArrayList<>() {{
        add(new LoginCheckFilter());
    }};

    public static boolean filterRequest(HttpRequest req) {
        for (Filter filter : filters) {
            if(!filter.checkReq(req))
                return false;
        }
        return true;
    }
}
