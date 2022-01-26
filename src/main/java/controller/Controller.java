package controller;

import http.HttpRequest;
import http.HttpResponse;

public interface Controller {

    void service(HttpRequest request, HttpResponse response);

    default void doGet(HttpRequest request, HttpResponse response) {
    }

    default void doPost(HttpRequest request, HttpResponse response) {
    }
}
