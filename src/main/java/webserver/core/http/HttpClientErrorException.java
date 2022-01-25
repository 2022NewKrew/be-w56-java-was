package webserver.core.http;

import webserver.core.http.response.HttpStatus;

public class HttpClientErrorException extends RuntimeException{
    private final HttpStatus status;
    public HttpClientErrorException(HttpStatus status, String msg){
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return this.status;
    }
}
