package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemoCreateDto {
    private int userId;
    private String content;
}
