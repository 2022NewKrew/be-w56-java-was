package controller.response;

import util.HttpStatus;

import java.util.Map;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오후 6:53
 */
public class Response {
    private HttpStatus httpStatus;
    private Map<String, String> responseHeader;
    private byte[] responseBody;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public static class Builder {
        private HttpStatus httpStatus;
        private Map<String, String> responseHeader;
        private byte[] responseBody;

        public Builder() {

        }

        public Builder ok() {
            httpStatus = HttpStatus.OK;
            return this;
        }

        public Builder redirect() {
            httpStatus = HttpStatus.REDIRECT;
            return this;
        }

        public Builder notFound() {
            httpStatus = HttpStatus.NOT_FOUND;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.responseHeader = headers;
            return this;
        }

        public Builder body(byte[] responseBody) {
            this.responseBody = responseBody;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }

    public Response(Builder responseBuilder) {
        httpStatus = responseBuilder.httpStatus;
        responseHeader = responseBuilder.responseHeader;
        responseBody = responseBuilder.responseBody;
    }
}
