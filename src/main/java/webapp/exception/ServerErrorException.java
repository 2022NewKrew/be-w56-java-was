package webapp.exception;

public class ServerErrorException extends CustomException{
    public ServerErrorException() {
        super(ErrorCode.SERVER_ERROR);
    }
}
