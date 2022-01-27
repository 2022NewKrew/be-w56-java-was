package webserver.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HttpResponseStartLine {
    private final String version;
    private final HttpStatus httpStatus;

    @Override
    public String toString() {
        return this.version + " " + this.httpStatus.toString() + "\r\n";
    }
}
