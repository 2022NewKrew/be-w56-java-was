package com.leoserver.controller;

import com.leoserver.dto.DefaultDTO;
import com.leoserver.model.User;
import com.leoserver.service.UserService;
import com.leoserver.webserver.annotation.Autowired;
import com.leoserver.webserver.annotation.Controller;
import com.leoserver.webserver.annotation.RequestBody;
import com.leoserver.webserver.annotation.RequestMapping;
import com.leoserver.webserver.annotation.RequestParam;
import com.leoserver.webserver.http.HttpHeaderOption.HeaderOptionName;
import com.leoserver.webserver.http.HttpStatus;
import com.leoserver.webserver.http.KakaoHttpHeader;
import com.leoserver.webserver.http.KakaoHttpResponse;
import com.leoserver.webserver.http.Location;
import com.leoserver.webserver.http.MIME;
import com.leoserver.webserver.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;


  @RequestMapping(method = Method.GET, path = "/user/create")
  public KakaoHttpResponse<DefaultDTO> createUser(
      @RequestParam("userId") String userId,
      @RequestParam("password") String password,
      @RequestParam("name") String name,
      @RequestParam("email") String email
  ) {

    logger.debug("userId : {}", userId);
    logger.debug("password : {}", password);
    logger.debug("name : {}", name);
    logger.debug("email : {}", email);

    User user = new User(userId, password, name, email);

    userService.createUser(user);

    return new KakaoHttpResponse<>(
        HttpStatus.OK,
        new DefaultDTO("회원이 생성되었습니다.", HttpStatus.OK.name())
    );
  }


  @RequestMapping(method = Method.POST, path = "/user/create")
  public KakaoHttpResponse<DefaultDTO> createUserWithRedirect(@RequestBody User user) {

    logger.debug("userId : {}", user.getUserId());
    logger.debug("password : {}", user.getPassword());
    logger.debug("name : {}", user.getName());
    logger.debug("email : {}", user.getEmail());

    userService.createUser(user);

    KakaoHttpHeader header = KakaoHttpHeader.createResponse();
    header.set(HeaderOptionName.LOCATION, new Location("/index.html"));
    header.set(HeaderOptionName.CONTENT_TYPE, MIME.APPLICATION_JSON);

    return new KakaoHttpResponse<>(
        HttpStatus.FOUND,
        new DefaultDTO("회원이 생성되었습니다.", HttpStatus.FOUND.name()),
        header
    );
  }


}
