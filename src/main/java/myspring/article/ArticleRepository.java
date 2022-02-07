package myspring.article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    void save(Article article);

    default void update(Article article) {}

    default void delete(Article article) {}

    default Optional<Article> findBySeq(long seq) { return Optional.empty(); }

    Optional<Article> findByTitle(String title);

    List<Article> findAll();

    default int CountAll() { return -1; }

}
