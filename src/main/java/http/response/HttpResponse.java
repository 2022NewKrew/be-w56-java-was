package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import util.Constant;
import view.ViewMaker;

public class HttpResponse {

    private final ResponseStatusLine statusLine;
    private final ResponseHeader header;
    private final ResponseBody body;
    private final DataOutputStream dos;

    public HttpResponse(ResponseStatusLine statusLine, ResponseHeader header,
            ResponseBody body, DataOutputStream dos) {
        this.statusLine = statusLine;
        this.header = header;
        this.body = body;
        this.dos = dos;
    }

    public static HttpResponse ok(String path, Map<String, String> model,
            Map<String, String> cookie, DataOutputStream dos) {
        byte[] view = ViewMaker.getView(path, model);

        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol,
                StatusCode.OK.getStatus());
        ResponseBody body = new ResponseBody(view);
        ResponseHeader header = new ResponseHeader();

        header.addContentType(path);
        header.addContentLength(body.getBodyLength());
        header.addCookie(cookie);

        return new HttpResponse(statusLine, header, body, dos);
    }

    public static HttpResponse found(String path, Map<String, String> cookie,
            DataOutputStream dos) {
        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol,
                StatusCode.FOUND.getStatus());
        ResponseHeader header = new ResponseHeader();

        header.addLocation(path);
        header.addCookie(cookie);

        return new HttpResponse(statusLine, header, ResponseBody.getEmptyBody(), dos);
    }

    public static HttpResponse unauthorized(String path, Map<String, String> model,
            DataOutputStream dos) {
        byte[] view = ViewMaker.getView(path, model);

        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol,
                StatusCode.UNAUTHORIZED.getStatus());
        ResponseBody body = new ResponseBody(view);
        ResponseHeader header = new ResponseHeader();

        header.addContentType(path);
        header.addContentLength(body.getBodyLength());

        return new HttpResponse(statusLine, header, body, dos);
    }

    public static HttpResponse notFound(DataOutputStream dos) {
        byte[] view = ViewMaker.getNotFoundView();

        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol,
                StatusCode.NOT_FOUND.getStatus());
        ResponseBody body = new ResponseBody(view);
        ResponseHeader header = new ResponseHeader();

        header.addContentType(ContentType.HTML);
        header.addContentLength(body.getBodyLength());

        return new HttpResponse(statusLine, header, body, dos);
    }

    public static HttpResponse badRequest(DataOutputStream dos) {
        byte[] view = ViewMaker.getBadRequestView();

        ResponseStatusLine statusLine = new ResponseStatusLine(Constant.protocol,
                StatusCode.BAD_REQUEST.getStatus());
        ResponseBody body = new ResponseBody(view);
        ResponseHeader header = new ResponseHeader();

        header.addContentType(ContentType.HTML);
        header.addContentLength(body.getBodyLength());

        return new HttpResponse(statusLine, header, body, dos);
    }

    public void sendResponse() throws IOException {
        sendStartLine();
        sendHeader();
        dos.writeBytes(Constant.lineBreak);
        sendBody();
        dos.flush();
    }

    private void sendStartLine() throws IOException {
        dos.writeBytes(statusLine.getLine());
    }

    private void sendHeader() throws IOException {
        dos.writeBytes(header.getComponentString());
    }

    private void sendBody() throws IOException {
        dos.write(body.getBody());
    }
}
