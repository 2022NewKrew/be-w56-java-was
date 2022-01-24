package webserver;

import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpClientErrorException;
import webserver.http.HttpStatus;

public class WebController {
    private static WebController webController;

    private WebController(){}

    public static WebController getInstance(){
        if (webController == null){
            webController = new WebController();
        }
        return webController;
    }

    public byte[] route(HttpRequest request){
        if(request.getUrl().equals("/index.html") && (request.getMethod() == HttpMethod.GET)){
            return index();
        }
//        if(request.getUrl().equals("/"))

        throw new HttpClientErrorException(HttpStatus.NotFound, request.getMethod() + "," + request.getUrl() + " 페이지를 찾을 수 없습니다.");
    }

    public byte[] index(){
        return TemplateEngine.getInstance().render("/index.html");
    }
}
