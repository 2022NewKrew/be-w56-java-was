package data;

import data.converter.ConverterServiceForTest;
import org.junit.jupiter.params.provider.Arguments;
import webserver.domain.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class MockPostData {
    public static Stream<Arguments> getFieldMapStream(){
        List<Post> posts = getAll();
        return ConverterServiceForTest.convertToFieldMapListArgumentsStream(posts);
    }

    public static List<Post> getAll() {
        return List.of(
                new Post("javajigi", "good java", LocalDateTime.now().minusDays(1)),
                new Post("good", "good good", LocalDateTime.now().minusDays(2)),
                new Post("goodWriter", "LGTM!", LocalDateTime.now().minusDays(3)),
                new Post("hello", "hello world!", LocalDateTime.now().minusDays(4))
        );
    }
}
