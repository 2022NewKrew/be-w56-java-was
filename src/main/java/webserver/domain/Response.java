package webserver.domain;

public class Response {
    private String header;
    private byte[] body;

    public Response() {
        this.body = "hello world".getBytes();
        this.header = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "\r\n";
    }

    public Response(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public Response(String header) {
        this.header = header;
        this.body = " ".getBytes();
    }

    public String getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
