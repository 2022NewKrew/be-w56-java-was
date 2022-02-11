package Controller;

import model.MyHttpRequest;
import model.MyHttpResponse;
import webserver.enums.HttpMethod;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {
    MyHttpResponse run(MyHttpRequest request, DataOutputStream dos) throws IOException;

    HttpMethod getMethod();

    String getUrl();
}
