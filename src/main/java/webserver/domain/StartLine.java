package webserver.domain;

import lombok.Builder;

public class StartLine {

    private static final String FILE_ROOT_PATH = "./webapp";
    private final static String START_LINE_SEPARATOR = " ";

    private final String httpMethod;
    private final String requestTarget;
    private final String httpVersion;

    @Builder
    private StartLine(String httpMethod, String requestTarget, String httpVersion) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.httpVersion = httpVersion;
    }

    public static StartLine createStartLine(String line) {
        String[] splited = line.split(START_LINE_SEPARATOR);
        return StartLine.builder()
            .httpMethod(splited[0])
            .requestTarget(splited[1])
            .httpVersion(splited[2])
            .build();
    }

    public String getRequestUrl() {
        return FILE_ROOT_PATH + requestTarget;
    }
}
