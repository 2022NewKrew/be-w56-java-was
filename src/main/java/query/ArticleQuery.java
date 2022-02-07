package query;

public class ArticleQuery {
    private ArticleQuery() {
        throw new IllegalStateException("Utility class");
    }

    public static final String INSERT_QUERY = "insert into ARTICLE (writer, title, contents) values (?,?,?)";
    public static final String FIND_ALL_QUERY = "select * from ARTICLE";
}
