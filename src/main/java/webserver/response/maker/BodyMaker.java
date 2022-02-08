package webserver.response.maker;

import view.ViewFinder;
import webserver.http.Response;

public class BodyMaker {

    public static byte[] make(Response response) {
        return ViewFinder.find(response.getPath(), response.getModel());
    }
}
