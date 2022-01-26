package controller;

import http.request.HttpRequest;
import java.util.Map;

public class StaticFileController implements Controller{

    private static StaticFileController instance;

    public static synchronized StaticFileController getInstance(){
        if(instance == null){
            instance = new StaticFileController();
        }
        return instance;
    }

    @Override
    public String run(HttpRequest request, Map<String, String> model) {

        return request.getUrl();
    }
}
