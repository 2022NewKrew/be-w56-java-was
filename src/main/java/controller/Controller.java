package controller;

import java.util.Map;

public interface Controller {

    public String doGet(Map<String, String> param);
    public String doPost(Map<String, String> param, Map<String, String> body);
}
