package mvc.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String password;
    private String name;
    private String email;
}
