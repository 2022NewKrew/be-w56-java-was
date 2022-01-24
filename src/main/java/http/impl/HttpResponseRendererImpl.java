package http.impl;

import http.HttpResponse;
import http.HttpResponseRenderer;
import webserver.exception.InternalServerErrorException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class HttpResponseRendererImpl implements HttpResponseRenderer {

    @Override
    public ByteArrayOutputStream render(HttpResponse response) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream ba = new DataOutputStream(byteArrayOutputStream);
        try {
            byte[] body = response.getResponseBody();
            ba.writeBytes("HTTP/1.1 200 OK \r\n");
            ba.writeBytes("Content-Length: " + body.length + "\r\n");
            ba.writeBytes("\r\n");
            ba.write(body, 0, body.length);
            ba.flush();
        } catch (IOException e) {
            throw new InternalServerErrorException(e.getClass().getName(), e);
        }
        return byteArrayOutputStream;
    }
}
