package model;

import db.DataBase;

public class SignUpService {
    private SignUpService() {

    }


    public static void signup(RequestHeader requestHeader) {
        User user = makeUser(requestHeader);
        if (DataBase.isExistUserId(user.getUserId())) {
            throw new IllegalStateException("DUPLICATED USER ID");
        }
        DataBase.addUser(makeUser(requestHeader));
    }

    private static User makeUser(RequestHeader requestHeader) {
        return User.builder()
                .userId(requestHeader.getParameter("userId"))
                .password(requestHeader.getParameter("password"))
                .name(requestHeader.getParameter("name"))
                .email((requestHeader.getParameter("email")))
                .build();
    }
}
