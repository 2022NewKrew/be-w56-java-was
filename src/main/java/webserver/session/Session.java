package webserver.session;

import model.UserAccount;

import java.time.LocalDateTime;

public class Session {
    private LocalDateTime validTime;
    private final UserAccount userAccount;

    public Session(UserAccount userAccount) {
        this.validTime = LocalDateTime.now().plusHours(6);
        this.userAccount = userAccount;
    }

    public int getMaxAge(){
        if(validTime.isAfter(LocalDateTime.now())){
            return 18000;
        }

        return 0;
    }

    public void invalidate(){
        validTime = LocalDateTime.now();
    }

    public String getSessionId(){
        return userAccount.getUserId();
    }

    public LocalDateTime getValidTime(){
        return validTime;
    }

    public UserAccount getUserAccount(){
        return userAccount;
    }
}
