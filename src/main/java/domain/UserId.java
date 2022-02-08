package domain;

import java.util.Objects;

public class UserId {
    private final String userId;

    public UserId(String userId) {
        valid(userId);
        this.userId = userId;
    }

    private void valid(String userId) {
        if (userId == null || userId.equals("")) {
            throw new NullPointerException("아이디를 입력하세요");
        }
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return userId;
    }
}
