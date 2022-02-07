package db;

import annotation.Bean;
import util.CheckedFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Bean
public class JdbcTemplate {

    private static final String MYSQL_URL = "was.mysql.url";
    private static final String MYSQL_USER = "was.mysql.username";
    private static final String MYSQL_PASS = "was.mysql.password";

    private final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public JdbcTemplate() {
        runSchemaScript();
    }

    public UpdateResult update(String sql, List<?> params) {
        return withStatement(sql, params, (statement) -> {
            int rows = statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            long key = rs.next() ? rs.getLong(1) : 0;
            return new UpdateResult(rows, key);
        });
    }

    public <T> T queryForObject(String sql, List<?> params, RowMapper<T> mapper) {
        return withStatement(sql, params, (statement) -> {
           ResultSet rs = statement.executeQuery();
           return mapper.map(rs);
        });
    }

    public <T> Stream<T> queryForStream(String sql, List<?> params, RowMapper<T> mapper) {
        return withStatement(sql, params, (statement) -> {
            ResultSet rs = statement.executeQuery();
            Stream.Builder<T> builder = Stream.builder();
            buildItem(rs, builder, mapper);
            return builder.build();
        });
    }

    private <T> void buildItem(ResultSet rs, Stream.Builder<T> builder, RowMapper<T> mapper) throws SQLException {
        while (rs.next()) {
            T item = mapper.map(rs);
            builder.add(item);
        }
    }

    private <T> T withStatement(String sql, List<?> params, CheckedFunction<PreparedStatement, T, SQLException> f) {
        return withConnection((connection) -> {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                setParams(statement, params);
                return f.apply(statement);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to run statement " + sql, e);
            }
        });
    }

    private void setParams(PreparedStatement statement, List<?> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            statement.setObject(i + 1, params.get(i));
        }
    }

    private <T> T withConnection(CheckedFunction<Connection, T, SQLException> f) {
        String url = System.getProperty(MYSQL_URL);
        String user = System.getProperty(MYSQL_USER);
        String pass = System.getProperty(MYSQL_PASS);
        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            return f.apply(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish DB connection", e);
        }
    }

    private void runSchemaScript() {
        String sql = readSchemaScript();
        withConnection((connection) -> {
           Statement statement = connection.createStatement();
           var foo = statement.execute(sql);
           return null;
        });
    }

    private String readSchemaScript() {
        try (InputStream is = loader.getResourceAsStream("db/sql/schema.sql")) {
            if (is == null) {
                return "";
            }
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
