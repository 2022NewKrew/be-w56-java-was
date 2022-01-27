package application.dto;

public class SignUpRequest {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private SignUpRequest(Builder builder) {
        this.userId = builder.userId;
        this.password = builder.password;
        this.name = builder.name;
        this.email = builder.email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public static class Builder {

        private String userId;
        private String password;
        private String name;
        private String email;

        public static Builder newInstance()
        {
            return new Builder();
        }

        private Builder() {}

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

        public SignUpRequest build() { return new SignUpRequest(this); }
    }
}
