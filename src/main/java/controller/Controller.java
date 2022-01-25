package controller;

import java.util.Map;

public interface Controller {
    String handleRequest(String requestMethod, String requestPath, Map<String, String> queryParams);
    String doPost(String requestPath);
    String doGet(String requestPath, Map<String, String> queryParams);
}
