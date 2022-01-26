package model.user;

public class Password {
    private final String password;

    public Password(String password) {
        valid(password);
        this.password = password;
    }

    private void valid(String password) {
        if (password == null) {
            throw new NullPointerException("비밀번호를 입력하세요.");
        }
    }

    @Override
    public String toString() {
        return password;
    }
}
