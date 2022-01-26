package util.request;

import java.util.Arrays;

public enum HttpVersion {
    VERSION_1_1("http/1.1");

    private String versionString;

    HttpVersion(String versionString) {
        this.versionString = versionString;
    }

    public static HttpVersion of(String versionString){
        return Arrays.stream(values())
                .filter(httpVersion -> httpVersion.versionString.equalsIgnoreCase(versionString))
                .findFirst()
                .orElseThrow();
    }
}
