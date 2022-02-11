package file;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import util.HttpRequestUtils;
import util.constant.Route;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class PathOfTest {
    private static final Path INDEX = Path.of(Route.BASE.getPath() + Route.INDEX.getPath());

    @ParameterizedTest(name = "path: {arguments}")
    @CsvSource(value = {"/,./webapp/index.html", "index.html,./webapp/index.html", "/user/list.html,./webapp//user/list.html"}, delimiter = ',')
    public void pathOfHtml(String url, Path expected) {
        assertThat(HttpRequestUtils.urlToFile(url)).isEqualTo(expected);
    }
}
