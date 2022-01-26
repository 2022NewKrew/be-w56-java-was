package model;

public class StartLine {

    public static StartLine of(String startLine) {
        String[] token = startLine.split(" ");

        return new StartLine(token[0], token[1], token[2]);
    }

    private final String httpMethod; // TODO - 메소드 같은 경우는 enum으로 처리해도 괜찮을 것 같음
    private final String requestTarget;
    private final String httpVersion;

    private StartLine(String httpMethod, String requestTarget, String httpVersion) {
        this.httpMethod = httpMethod;
        this.requestTarget = requestTarget;
        this.httpVersion = httpVersion;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    @Override
    public String toString() {
        return "StartLine{" +
                "httpMethod='" + httpMethod + '\'' +
                ", requestTarget='" + requestTarget + '\'' +
                ", httpVersion='" + httpVersion + '\'' +
                '}';
    }
}
