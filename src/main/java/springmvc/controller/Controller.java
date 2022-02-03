package springmvc.controller;

import java.util.Map;

public interface Controller {

    public String doGet(Map<String, String> param, Map<String, String> sessionCookie);
    public String doPost(Map<String, String> param, Map<String, String> body, Map<String, String> sessionCookie);
}
