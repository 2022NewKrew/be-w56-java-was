package webserver.response;

import mapper.ResponseSendDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponse {
    private final HttpResponseHeader httpResponseHeader;
    private final HttpResponseBody httpResponseBody;
    private final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public HttpResponse(HttpResponseHeader httpResponseHeader, HttpResponseBody httpResponseBody) {
        this.httpResponseHeader = httpResponseHeader;
        this.httpResponseBody = httpResponseBody;
    }

    public static HttpResponse makeHttpResponse(ResponseSendDataModel model) throws IOException{
        HttpResponseBody httpResponseBody = new HttpResponseBody();
        if(!model.getName().matches("redirect:/.*"))
            httpResponseBody = HttpResponseBody.makeHttpResponseBody(model);
        HttpResponseHeader httpResponseHeader = HttpResponseHeader.makeHttpResponseHeader(model, httpResponseBody.getBody().length);

        return new HttpResponse(httpResponseHeader, httpResponseBody);
    }

    public void writeHttpResponse(DataOutputStream dos) throws IOException{
        dos.write(this.httpResponseHeader.getHeader(), 0, this.httpResponseHeader.getHeader().length);
        dos.write(this.httpResponseBody.getBody(), 0, this.httpResponseBody.getBody().length);
        dos.flush();
    }
}
