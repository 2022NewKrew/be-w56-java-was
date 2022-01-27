package webserver.http;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum MimeType {
    HTML("html", MimeSubtype.TEXT_HTML),
    CSS("css", MimeSubtype.TEXT_CSS),
    JAVASCRIPT("js", MimeSubtype.TEXT_JS),
    JSON("json", MimeSubtype.APPLICATION_JSON),
    ;

    private static final Map<String, MimeSubtype> mimeMap;

    static {
        mimeMap = new HashMap<>();
        Arrays.stream(values()).forEach(value -> mimeMap.put(value.extension, value.mimeSubtype));
    }

    private final String extension;
    private final MimeSubtype mimeSubtype;

    MimeType(String extension, MimeSubtype mimeSubtype) {
        this.extension = extension;
        this.mimeSubtype = mimeSubtype;
    }

    public static MimeSubtype getMimeSubtype(String extension) {
        MimeSubtype mimeSubType = mimeMap.get(extension);
        if (mimeSubType == null) {
            throw new IllegalArgumentException("Content type을 찾을 수 없습니다.");
        }
        return mimeSubType;
    }
}
