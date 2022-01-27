package webserver.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionMessage {
    HTTP_METHOD_NOT_FOUND_EXCEPTION("HTTP 메소드가 없습니다."),
    UNSUPPORTED_HTTP_METHOD_EXCEPTION("현재는 지원되지 않는 메소드입니다."),
    URL_NOT_FOUND_EXCEPTION("URL이 없습니다."),
    HTTP_VERSION_NOT_FOUND_EXCEPTION("HTTP 버전이 없습니다."),
    INVALID_HTTP_VERSION_EXCEPTION("잘못된 http 버전입니다."),
    HTTP_STATUS_NOT_FOUND_EXCEPTION("해당하는 HTTP 상태 코드가 없습니다.");

    private final String message;
}
