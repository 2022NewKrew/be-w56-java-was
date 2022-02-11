package dto.mapper;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCookieDto {
    private int id;
    private String name;
}
