package webserver.response.maker;


import webserver.http.Response;
import webserver.http.response.HttpResponse;

public class ResponseMaker {

    public static HttpResponse make(Response response, String httpVersion) {
        byte[] body = BodyMaker.make(response);
        String header = HeaderMaker.make(response, httpVersion, body.length);

        return new HttpResponse(header, body);
    }
}
