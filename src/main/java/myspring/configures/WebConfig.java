package myspring.configures;

import myspring.configures.web.ArticleRequestHandlerMethodArgumentResolver;
import myspring.configures.web.ReplyRequestHandlerMethodArgumentResolver;
import myspring.configures.web.SimplePageRequestHandlerMethodArgumentResolver;
import myspring.interceptor.LoginInterceptor;
import org.apache.commons.dbcp2.BasicDataSource;
import webserver.annotations.Bean;
import webserver.annotations.Configuration;
import webserver.configures.HandlerMethodArgumentResolver;
import webserver.configures.InterceptorRegistry;
import webserver.configures.WebMvcConfigurer;
import webserver.jdbc.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://211.218.215.226:3307/kakao?useUnicode=true&serverTimezone=Asia/Seoul");
        //dataSource.setDriverClassName("org.h2.Driver");
        //dataSource.setUrl("jdbc:h2:mem:default");
        dataSource.setUsername("guest");
        dataSource.setPassword("1234");

        try (Connection conn = dataSource.getConnection()){
            Statement stmt = conn.createStatement();
            String schemaStmt = Files.readString(Path.of(getClass().getClassLoader().getResource("schema.sql").getPath().substring(1))).replaceAll("--.*\r", "").replaceAll("[\r\n]","");
            String dataStmt = Files.readString(Path.of(getClass().getClassLoader().getResource("data.sql").getPath().substring(1))).replaceAll("--.*\r", "").replaceAll("[\r\n]","");
            for (String sql : schemaStmt.split(";")) stmt.executeUpdate(sql);
            for (String sql : dataStmt.split(";")) stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(Arrays.asList("/qna/**"))
                .excludePathPatterns();
    }

    @Bean
    public ArticleRequestHandlerMethodArgumentResolver ArticleRequestHandlerMethodArgumentResolver() {
        return new ArticleRequestHandlerMethodArgumentResolver();
    }

    @Bean
    public ReplyRequestHandlerMethodArgumentResolver ReplyRequestHandlerMethodArgumentResolver() {
        return new ReplyRequestHandlerMethodArgumentResolver();
    }

    @Bean
    public SimplePageRequestHandlerMethodArgumentResolver SimplePageRequestHandlerMethodArgumentResolver() {
        return new SimplePageRequestHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(ArticleRequestHandlerMethodArgumentResolver());
        argumentResolvers.add(ReplyRequestHandlerMethodArgumentResolver());
        argumentResolvers.add(SimplePageRequestHandlerMethodArgumentResolver());
    }

}
