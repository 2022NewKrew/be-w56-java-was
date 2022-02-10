package webserver;

import javax.sql.DataSource;

public interface DataSourceFactory {

    DataSource getDataSource();
}
