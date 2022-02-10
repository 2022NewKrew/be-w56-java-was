package controller.response;

import util.HttpStatus;

import javax.ws.rs.core.AbstractMultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오후 6:53
 */
public class Response {
    private HttpStatus httpStatus;
    private AbstractMultivaluedMap<String, String> responseHeader;
    private byte[] responseBody;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public AbstractMultivaluedMap<String, String> getResponseHeader() {
        return responseHeader;
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public static class Builder {
        private HttpStatus httpStatus;
        private AbstractMultivaluedMap<String, String> responseHeader;
        private byte[] responseBody;

        public Builder() {
            responseHeader = new MultivaluedHashMap<>();
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

        public Builder header(String key, String value) {
            responseHeader.add(key, value);
            return this;
        }
        public Builder headers(AbstractMultivaluedMap<String, String> headers) {
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
