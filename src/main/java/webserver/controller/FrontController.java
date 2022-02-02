package webserver.controller;

import annotation.AnnotationProcessor;
import exception.UnAuthorizedException;
import webserver.http.HttpConst;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;


public class FrontController {
    private static final FrontController frontController = new FrontController();

    private FrontController() {
    }

    public static FrontController getInstance() {
        return frontController;
    }

    public HttpResponse process(HttpRequest request) {
        try {
            HttpResponse response = (HttpResponse) AnnotationProcessor.getInstance().requestMappingProcessor(request);

            if (response == null) {
                response = new HttpResponse(HttpStatus.OK, request.getUrl());
            }

            return response;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new HttpResponse(HttpStatus.BAD_REQUEST, HttpConst.ERROR_PAGE);
        } catch (UnAuthorizedException e) {
            e.printStackTrace();
            return new HttpResponse(HttpStatus.UNAUTHORIZED, HttpConst.LOGIN_PAGE);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpConst.ERROR_PAGE);
        }
    }
}
