package frontcontroller;

import util.MyHttpRequest;
import util.MyHttpResponse;
import util.MySession;

import java.io.IOException;
import java.sql.SQLException;

public interface MyController {

    ModelView process(MyHttpRequest request, MyHttpResponse response, MySession session) throws IOException, SQLException;
}
