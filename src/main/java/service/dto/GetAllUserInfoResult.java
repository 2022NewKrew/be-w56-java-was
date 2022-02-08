package service.dto;

import java.util.Collections;
import java.util.List;

public class GetAllUserInfoResult {

    private final List<GetUserInfoResult> userInfos;

    public GetAllUserInfoResult(List<GetUserInfoResult> userInfos) {
        this.userInfos = userInfos;
    }

    public List<GetUserInfoResult> getUserInfos() {
        return Collections.unmodifiableList(userInfos);
    }
}
