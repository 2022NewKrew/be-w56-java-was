package entity;

public class User {
    private final Long id;
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public static class Builder {
        // 필수 매개변수
        private String userId;
        private String password;
        private String name;
        private String email;

        // 선택 매개변수
        private final Long id = 0L;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public static Builder builder() {
        return new Builder();
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

    public String getPassword() {
        return password;
    }
}
