package webserver.filter;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.ArrayList;
import java.util.List;

public class FilterChain {
    private static List<Filter> filters = new ArrayList<>() {{
        add(new LoginCheckFilter());
    }};

    public static boolean filter(HttpRequest req, HttpResponse res) {
        for (Filter filter : filters) {
            if(!filter.checkReq(req)) {
                filter.failResponse(res);
                return false;
            }
        }
        return true;
    }
}
