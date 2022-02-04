package configure;

import app.configure.DbConfigure;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class DbConfigureTest {

    @Test
    void dbConfigureTest() throws SQLException {
        new DbConfigure();
    }
}
