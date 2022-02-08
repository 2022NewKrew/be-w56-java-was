package webserver.controller;

import DTO.RequestHeader;
import DTO.ResponseHeader;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberController implements Controller {

    static {
        Controller joinController = new JoinController();
        controllers.add(joinController);
    }

    // @ RequestMapping(CREATE_USER)
    static class JoinController implements Controller{
        final private String url = "/user/create";

        @Override
        public ResponseHeader getResponse(RequestHeader requestHeader, ResponseHeader responseHeader){
            Map<String, String> requestParam = requestHeader.getBody();
            String requestUrl= requestHeader.getRequestUrl();
            User user = User.join(requestParam);
            log.info("Create New User : {}", user);
            log.info("Url Changed: {}", requestUrl);

            responseHeader.setRedirect("/");
            return responseHeader;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }

}
