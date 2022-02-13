package model.user;

import error.ErrorMessages;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Embeddable
public class Password {

    private String password;
    private final static String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z0-9/$@$!%*#?&]{8,}$";

    public Password(String password) {
        if (!password.matches(PASSWORD_PATTERN))
            throw new IllegalArgumentException(ErrorMessages.WRONG_PASSWORD_FORMAT);
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password compare = (Password) o;
        return Objects.equals(password, compare.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
