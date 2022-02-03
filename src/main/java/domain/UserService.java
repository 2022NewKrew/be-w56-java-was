package domain;

import db.DataBase;
import domain.User;
import domain.dto.LoginDto;

public class UserService {

    public final DataBase dataBase;

    public UserService(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void addUser(User user) {
        dataBase.addUser(user);
    }

    public boolean login(LoginDto loginUser) {
        User findUser;
        findUser = dataBase.findUserById(loginUser.getUserId());
        if ( findUser == null || !findUser.getPassword().equals(loginUser.getPassword()) )
            return false;
        return true;
    }
}
