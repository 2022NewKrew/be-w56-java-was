package webserver.filter;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

import java.util.List;

public abstract class Filter {
    protected List<String> blackList;

    public abstract boolean checkReq(HttpRequest req);

    public abstract void failResponse(HttpResponse res);
}
