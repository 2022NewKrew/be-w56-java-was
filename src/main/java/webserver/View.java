package webserver;

import java.io.OutputStream;

public interface View {

    void render(ModelAndView mv, OutputStream out);
}
