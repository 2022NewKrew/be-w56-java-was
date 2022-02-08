package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoDto {
    private Long memoId;
    private String writerId;
    private String writerName;
    private String content;
    private LocalDateTime regDate;
}
