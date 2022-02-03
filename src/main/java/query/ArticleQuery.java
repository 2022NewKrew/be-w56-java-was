package query;

public class ArticleQuery {
    public static String INSERT_QUERY = "insert into ARTICLE (writer, title, contents) values (?,?,?)";
    public static String FIND_ALL_QUERY = "select * from ARTICLE";
}
