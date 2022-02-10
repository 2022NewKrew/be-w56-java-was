package adaptor.out.persistence.user;

import application.out.user.SignUpUserPort;
import application.out.user.UserDao;
import domain.user.User;

public class SignUpUserAdaptor implements SignUpUserPort {

    private final UserDao userDao;

    public SignUpUserAdaptor(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }
}
