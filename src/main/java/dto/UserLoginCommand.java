package dto;

public class UserLoginCommand {
    private final String userId;
    private final String password;

    private UserLoginCommand(Builder builder) {
        this.userId = builder.userId;
        this.password = builder.password;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private String userId;
        private String password;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public UserLoginCommand build() { return new UserLoginCommand(this); }
    }
}
