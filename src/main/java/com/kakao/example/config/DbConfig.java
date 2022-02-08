package com.kakao.example.config;

import framework.util.annotation.Bean;
import framework.util.annotation.Component;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Properties;

import static framework.util.Constants.CLASS_PATH;
import static framework.util.annotation.Component.ComponentType.CONFIGURATION;

@Component(type = CONFIGURATION)
public class DbConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbConfig.class);

    /**
     * DB 연동을 위한 DataSource형 객체를 반환해주는 메소드, Bean으로 등록하여 Container에 저장
     * @return DataSource형 객체
     */
    @Bean
    public DataSource dataSource() {
        Properties properties = new Properties();
        properties.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://10.202.177.51:3306/kakaodb?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul");
        properties.setProperty("username", "myles.nah");
        properties.setProperty("password", "Blackbird92#");

        DataSource dataSource = null;

        try {
            dataSource = BasicDataSourceFactory.createDataSource(properties);

            Connection conn = dataSource.getConnection();
            ScriptRunner sr = new ScriptRunner(conn);

            // Schema와 Data 초기화
            sr.runScript(new BufferedReader(new FileReader(CLASS_PATH + "sql/schema.sql")));
            sr.runScript(new BufferedReader(new FileReader(CLASS_PATH + "sql/data.sql")));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return dataSource;
    }
}
