package com.kakao.example;

import framework.webserver.WebServer;

public class ExampleApplication {
    public static void main(String[] args) {
        WebServer.run(ExampleApplication.class, args);
    }
}
