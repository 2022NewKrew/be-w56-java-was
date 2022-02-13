package model;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Builder
@Getter
public class Memo {
    @Builder.Default
    private int id = -1;
    private int userId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    public Boolean isNew(){
        return id == -1;
    }
}
