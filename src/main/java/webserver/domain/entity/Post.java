package webserver.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Post {
    private Long id;
    private final String writer;
    private final String content;
    private final LocalDateTime written;
}
