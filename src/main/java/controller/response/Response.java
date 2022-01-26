package controller.response;

import util.HttpStatus;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오후 6:53
 */
public class Response {
    private HttpStatus httpStatus;
    private String responseUrl;

    public Response(HttpStatus httpStatus, String responseUrl) {
        this.httpStatus = httpStatus;
        this.responseUrl = responseUrl;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getResponseUrl() {
        return responseUrl;
    }
}
