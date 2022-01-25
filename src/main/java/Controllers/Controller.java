package Controllers;

import model.Request;
import model.Response;

import java.io.IOException;

public abstract class Controller {

    public void service(Request request, Response response) throws IOException {
        String httpMethod = request.getRequestHeader().getRequestLine().getHttpMethod();
        if(httpMethod.equals("GET")){
            getMethod(request, response);
        }
        else if(httpMethod.equals("POST")){
            postMethod(request, response);
        }
    }
    abstract void getMethod(Request request, Response response) throws IOException;

    abstract void postMethod(Request request, Response response);
}
