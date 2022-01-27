package com.leoserver.service;

import com.leoserver.db.DataBase;
import com.leoserver.model.User;
import com.leoserver.webserver.annotation.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

  public final Logger logger = LoggerFactory.getLogger(UserService.class);

  public void createUser(User user) {
    DataBase.addUser(user);
  }


  public Optional<User> login(String id, String password) {

    Optional<User> findUser = DataBase.findUserById(id);

    // no id
    if (findUser.isEmpty()) {
      return Optional.empty();
    }

    // no password
    User user = findUser.get();
    if (!user.getPassword().equals(password)) {
      return Optional.empty();
    }

    return Optional.of(user);
  }


}
