package webserver.http.request;

public class NullRequestException extends Exception{

    public NullRequestException() {
        super("No data received...");
    }
}
