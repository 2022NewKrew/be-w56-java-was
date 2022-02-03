package model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Article {
    private Long id;
    private String writer;
    private String title;
    private String contents;

}