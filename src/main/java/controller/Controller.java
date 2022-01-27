package controller;

import controller.exception.ControllerMismatchException;
import http.HttpRequest;
import http.HttpResponse;
import http.HttpStatus;

public interface Controller {

    default HttpResponse doGet(HttpRequest request) {
        throw new ControllerMismatchException("Request Path :: " + request.getPath());
    }

    default HttpResponse doPost(HttpRequest request) {
        return notImplemented();
    }

    default HttpResponse doPut(HttpRequest request) {
        return notImplemented();
    }

    default HttpResponse doDelete(HttpRequest request) {
        return notImplemented();
    }

    default HttpResponse badRequest() {
        return HttpResponse.builder()
            .status(HttpStatus.BAD_REQUEST)
            .build();
    }

    default HttpResponse notImplemented() {
        return HttpResponse.builder()
            .status(HttpStatus.NOT_IMPLEMENTED)
            .build();
    }
}
