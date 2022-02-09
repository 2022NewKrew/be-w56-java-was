package application.domain;

import application.db.DataBase;
import application.domain.dto.LoginDto;

import java.util.List;

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

    public List<User> getUserList() {
        return dataBase.findAll();
    }
}
