package db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

class DataSourceUtilsTest {

    @Test
    @DisplayName("DB에 연결 요청하여 Connection 가져오기")
    void getConnection() throws SQLException {
        //given

        //when
        Connection con = DataSourceUtils.getConnection();

        //then
        assertThat(con).isNotNull();
    }
}