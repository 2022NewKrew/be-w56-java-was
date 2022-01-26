package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateDto {
    private String stringId;
    private String password;
    private String name;
    private String email;
}
