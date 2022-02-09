package model;

import exceptions.InvalidHttpVersionException;

public enum HttpVersion {
    HTTP_0_9("HTTP/0.9"),
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1");

    public static HttpVersion fromString(String version) {
        for (HttpVersion hv : HttpVersion.values()) {
            if (hv.getVersion().equals(version)) {
                return hv;
            }
        }
        throw new InvalidHttpVersionException("해당 http 버젼이 존재하지 않습니다");
    }

    private final String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
