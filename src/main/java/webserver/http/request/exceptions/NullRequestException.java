package webserver.http.request.exceptions;

public class NullRequestException extends Exception {

    public NullRequestException() {
        super("No data received...");
    }
}
