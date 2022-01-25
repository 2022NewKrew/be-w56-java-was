package model;

public class UserAccountDTO {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    private UserAccountDTO(Builder builder) {
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

    public static class Builder{
        private String userId;
        private String password;
        private String name;
        private String email;

        public Builder(){
            userId = "";
            password = "";
            name = "";
            email = "";
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserAccountDTO build(){
            return new UserAccountDTO(this);
        }
    }
}
