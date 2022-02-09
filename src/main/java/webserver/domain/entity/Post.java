package webserver.domain.entity;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class Post {
    private int id;
    private final String writer;
    private final String content;
    private final LocalDateTime written;
}
