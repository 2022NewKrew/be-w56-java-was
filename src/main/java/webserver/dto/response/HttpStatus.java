package webserver.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum HttpStatus {
    OK(200),
    FOUND(302);

    private final int code;

    @Override
    public String toString() {
        return this.code + " " + this.name();
    }
}
