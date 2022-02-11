package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private int id;
    private String stringId;
    private String name;
    private String email;
}
