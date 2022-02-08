package com.kakao.example.model.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Setter
    private long memoId;

    private String writerId;
    private String content;

    @Setter
    private LocalDateTime createdDate;

    @Setter
    private String formattedCreatedDate;
}
