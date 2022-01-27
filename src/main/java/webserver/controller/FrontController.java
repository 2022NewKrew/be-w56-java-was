package webserver.controller;

import annotation.AnnotationProcessor;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import exception.UnAuthorizedException;
import webserver.http.*;


public class FrontController{
    private static FrontController frontController;
    private final AnnotationProcessor annotationProcessor = new AnnotationProcessor();
    private FrontController(){
    }

    public static FrontController getInstance(){
        if(frontController == null){
            frontController = new FrontController();
        }
        return frontController;
    }

    public HttpResponse process(HttpRequest request){
        try{
            HttpResponse response = (HttpResponse) annotationProcessor.requestMappingProcessor(request);

            if(response == null){
                response = new HttpResponse(HttpStatus.OK, request.getUrl());
            }

            return response;

        } catch(IllegalArgumentException e){
            e.printStackTrace();
            return new HttpResponse(HttpStatus.BAD_REQUEST, HttpConst.ERROR_PAGE);
        } catch (UnAuthorizedException e){
            e.printStackTrace();
            return new HttpResponse(HttpStatus.UNAUTHORIZED, HttpConst.LOGIN_PAGE);
        }
        catch(Exception e){
            e.printStackTrace();
            return new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpConst.ERROR_PAGE);
        }
    }
}
