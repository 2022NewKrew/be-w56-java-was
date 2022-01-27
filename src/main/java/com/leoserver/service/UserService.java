package com.leoserver.service;

import com.leoserver.db.DataBase;
import com.leoserver.model.User;
import com.leoserver.webserver.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

  public final Logger logger = LoggerFactory.getLogger(UserService.class);

  public void createUser(User user) {
    DataBase.addUser(user);
  }


}
