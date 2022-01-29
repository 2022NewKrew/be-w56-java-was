package http;

public class RequestMessage {
    private final RequestLine requestLine;
    private final Headers headers;

    public RequestMessage(RequestLine requestLine, Headers headers) {
        validateNull(requestLine, headers);
        this.requestLine = requestLine;
        this.headers = headers;
    }

    private void validateNull(RequestLine requestLine, Headers headers) {
        if(requestLine == null || headers == null) {
            throw new IllegalArgumentException();
        }
    }

//    public byte[] readStaticFile() throws IOException {
//        File file =  requestLine.findStaticFile();
//        return Files.readAllBytes(file.toPath());
//    }

    public RequestLine getStatusLine() {
        return requestLine;
    }
}
