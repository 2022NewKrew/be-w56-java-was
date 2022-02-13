package dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemoResponseDto {
    private int id;
    private String content;
    private String writer;
    private LocalDateTime createdAt;
}
