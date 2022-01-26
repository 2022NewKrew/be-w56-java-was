package util;

public class ResponseData {
    private String header;
    private byte[] body;
    public ResponseData() {
        this.body = "hello world".getBytes();
        this.header = "HTTP/1.1 200 OK \r\n"+
                "Content-Type: text/html;charset=utf-8\r\n"+
                "Content-Length: " + body.length + "\r\n"+
                "\r\n";
    }
    public ResponseData(String header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() { return header; }
    public byte[] getBody() { return body; }
}
