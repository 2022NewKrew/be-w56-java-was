package webserver.context;

import users.UserDto;

public class HttpSession {

    public Object getAttribute(String key){
        return new Object();
    }

    public void setAttribute(String sessionedUser, UserDto userDto) {
    }

    public void invalidate() {
    }
}
