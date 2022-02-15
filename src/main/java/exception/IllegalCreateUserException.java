package exception;

public class IllegalCreateUserException extends RuntimeException{

    public IllegalCreateUserException(){
        super();
    }

    public IllegalCreateUserException(String message){
        super(message);
    }

}
