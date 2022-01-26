package infrastructure.model;

import java.util.Objects;

public class ResponseLine {

    private final HttpStatus httpStatus;

    public ResponseLine(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static ResponseLine valueOf(HttpStatus httpStatus) {
        return new ResponseLine(httpStatus);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseLine that = (ResponseLine) o;
        return httpStatus == that.httpStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus);
    }

    @Override
    public String toString() {
        return "ResponseLine{" +
                "httpStatus=" + httpStatus +
                '}';
    }
}
