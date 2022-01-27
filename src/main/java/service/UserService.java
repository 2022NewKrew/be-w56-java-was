package service;

import DTO.SignUpDTO;
import db.DataBase;

public class UserService {
    //TODO throw
    public static void SignUp(SignUpDTO signUpDTO) {
        DataBase.addUser(signUpDTO.makeUser());
    }
}
