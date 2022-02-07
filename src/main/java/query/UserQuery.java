package query;

public class UserQuery {
    private UserQuery() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FIND_BY_ID_QUERY = "select * from USER where userid = ?";
    public static final String INSERT_QUERY = "insert into USER (userid, password, name, email) values (?,?,?,?)";
    public static final String FIND_ALL_QUERY = "select * from USER";
}
