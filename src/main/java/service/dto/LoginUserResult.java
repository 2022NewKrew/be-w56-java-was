package service.dto;

public class LoginUserResult {

    private final boolean isSame;

    public LoginUserResult(boolean isSame) {
        this.isSame = isSame;
    }

    public boolean isSame() {
        return isSame;
    }
}
