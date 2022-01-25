package entity;

public class User {
    private final Long id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public static class Builder {
        // 필수 매개변수
        private final String userId;
        private final String password;
        private final String name;
        private final String email;

        // 선택 매개변수
        private final Long id = 0L;

        public Builder(String userId, String password, String name, String email) {
            this.userId = userId;
            this.password = password;
            this.name = name;
            this.email = email;
        }

        public User build() {
            return new User(this);
        }
    }

    private User(Builder builder) {
        id = builder.id;
        userId = builder.userId;
        password = builder.password;
        name = builder.name;
        email = builder.email;
    }

    public String getUserId() {
        return userId;
    }
}
