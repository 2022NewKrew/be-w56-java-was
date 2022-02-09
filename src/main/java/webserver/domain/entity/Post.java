package webserver.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class Post {
    private int id;
    private final String writer;
    private final String content;
    private final LocalDateTime written;
}
