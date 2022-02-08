package service.dto;

public class GetUserInfoResult {

    private final String userId;
    private final String email;
    private final String name;

    public GetUserInfoResult(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
