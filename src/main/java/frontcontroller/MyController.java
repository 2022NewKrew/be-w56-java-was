package frontcontroller;

import util.MyHttpRequest;
import util.MyHttpResponse;

import java.io.IOException;

public interface MyController {

    void process(MyHttpRequest request, MyHttpResponse response) throws IOException;
}
