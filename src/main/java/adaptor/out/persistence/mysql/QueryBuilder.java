package adaptor.out.persistence.mysql;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QueryBuilder {

    private final String table;
    private final List<String> fields;

    public QueryBuilder(String table, List<String> fields) {
        this.table = table;
        this.fields = fields;
    }

    public String selectAll() {
        return String.valueOf(new StringBuilder("SELECT ")
                .append(String.join(", ", fields))
                .append(" FROM ")
                .append(table));
    }

    public String selectOne(String... fields) {
        return String.valueOf(
                new StringBuilder(selectAll())
                        .append(" WHERE ")
                        .append(Arrays.stream(fields)
                                        .map(e -> e.concat(" = ? "))
                                        .collect(Collectors.joining(" and "))
                        )
                        .append(" LIMIT 1")
        );
    }

    public String insertOne(String... fields) {
        return String.valueOf(new StringBuilder("INSERT INTO ")
                .append(table)
                .append(" ( ")
                .append(String.join(", ", fields))
                .append(" ) VALUES (")
                .append(IntStream.range(0, fields.length)
                        .mapToObj(e -> " ? ")
                        .collect(Collectors.joining(", ")))
                .append(")")
        );
    }

}
