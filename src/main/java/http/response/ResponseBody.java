package http.response;

public class ResponseBody {
    private final byte[] content;

    private ResponseBody(byte[] content) {
        this.content = content;
    }

    public static ResponseBody of(byte[] viewByteArray) {
        return new ResponseBody(viewByteArray);
    }

    public static ResponseBody empty() {
        return new ResponseBody(new byte[0]);
    }

    public byte[] getContent() {
        return content;
    }

    public int getLength() {
        return content.length;
    }
}
