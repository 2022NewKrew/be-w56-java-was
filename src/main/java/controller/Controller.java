package controller;

import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

public interface Controller {

    void service(HttpRequest request, HttpResponse response) throws IOException;

    default void doGet(HttpRequest request, HttpResponse response) throws IOException {
    }

    default void doPost(HttpRequest request, HttpResponse response) {
    }
}
