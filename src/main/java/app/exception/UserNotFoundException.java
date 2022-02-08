package app.exception;

import static util.Constant.MESSAGE_USER_NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.http.HttpStatus;

public class UserNotFoundException extends CustomException{
    private static final Logger log = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException() {
        super(MESSAGE_USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
}
