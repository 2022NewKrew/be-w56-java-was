package com.kakao.example.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemoDto {
    private long memoId;
    private String writerId;
    private String content;
    private String formattedCreatedDate;
}
