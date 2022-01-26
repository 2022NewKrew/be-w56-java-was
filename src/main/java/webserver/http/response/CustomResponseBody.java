package webserver.http.response;

public class CustomResponseBody {
    private byte[] body;

    public void writeBody(byte[] bodyContent) {
        body = bodyContent;
    }

    public byte[] getBodyContent() {
        return body;
    }
}
