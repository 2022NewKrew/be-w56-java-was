package db;

import db.mapper.UserRowMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.domain.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MyJdbcTemplateTest {

    @Test
    @DisplayName("query")
    void query(){
        //given
        UserRowMapper userRowMapper = new UserRowMapper();

        //when
        List<User> users = MyJdbcTemplate.query("select * from member", userRowMapper);

        //then
        assertThat(users.size()).isEqualTo(4);
    }
}