package query;

public class UserQuery {
    public static String FIND_BY_ID_QUERY = "select * from USER where userid = ?";
    public static String INSERT_QUERY = "insert into USER (userid, password, name, email) values (?,?,?,?)";
    public static String FIND_ALL_QUERY = "select * from USER";
}
