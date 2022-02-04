package com.kakao.http.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor
public class HttpCookie {
    private final String name;
    private final String value;
    private String path;

    @Override
    public String toString() {
        if (path == null || path.isEmpty()) {
            return String.format("%s=%s", this.name, this.value);
        }
        return String.format("%s=%s; Path=%s", this.name, this.value, this.path);
    }
}
