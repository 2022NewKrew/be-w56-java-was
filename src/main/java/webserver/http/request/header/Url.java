package webserver.http.request.header;

import lombok.Getter;

@Getter
public class Url {
    private static final int PATH_INDEX = 0;
    private static final int REQUEST_HEADER_FULL_PATH_INDEX = 1;

    private final String path;
    private final String fullPath;

    public Url(String inputHeader) {
        fullPath = getFullPathByHeader(inputHeader);
        path = getPathByFullPath(fullPath);
    }

    private static String getFullPathByHeader(String inputHeader) {
        return inputHeader.split(" ")[REQUEST_HEADER_FULL_PATH_INDEX];
    }

    private static String getPathByFullPath(String fullPath) {
        return fullPath.split("\\?")[PATH_INDEX];
    }
}
