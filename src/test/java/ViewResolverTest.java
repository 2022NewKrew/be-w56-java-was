import org.junit.jupiter.api.Test;
import webserver.Response;
import webserver.view.ModelAndView;
import webserver.view.ViewResolver;
import java.io.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ViewResolverTest {
    @Test
    void detectMimeTypeByTike() throws IOException, IllegalAccessException {
        ViewResolver viewResolver = new ViewResolver();
        OutputStream outputStream = new ByteArrayOutputStream();
        Response response = new Response(outputStream);
        ModelAndView mv = new ModelAndView();

        mv.setViewName("/index.html");
        viewResolver.resolve(response, mv);
        assertThat(outputStream.toString()).contains("Content-Type: text/html;charset=utf-8");

        outputStream = new ByteArrayOutputStream();
        response = new Response(outputStream);
        mv.setViewName("/css/styles.css");
        viewResolver.resolve(response, mv);
        assertThat(outputStream.toString()).contains("Content-Type: text/css;charset=utf-8");
    }
}
