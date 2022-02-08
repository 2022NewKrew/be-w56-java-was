package model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Memo {
    private Long memoId;
    private User writer;
    private String content;
    private LocalDateTime regDate;
}
