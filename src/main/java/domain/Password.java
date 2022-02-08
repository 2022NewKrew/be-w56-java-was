package domain;

import java.util.Objects;

public class Password {
    private final String password;

    public Password(String password) {
        valid(password);
        this.password = password;
    }

    private void valid(String password) {
        if (password == null || password.equals("")) {
            throw new NullPointerException("비밀번호를 입력하세요.");
        }
    }

    @Override
    public String toString() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
