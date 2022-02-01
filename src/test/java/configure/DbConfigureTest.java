package configure;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class DbConfigureTest {

    @Test
    void dbConfigureTest() throws SQLException {
        new DbConfigure();
    }
}
