package webserver.method;

import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.impl.GenericObjectPool;
import webserver.DataSourceFactory;
import webserver.SingletonBeanRegistry;
import webserver.VaultManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;

public class MysqlDataSourceFactory implements DataSourceFactory {

    @Override
    public DataSource getDataSource() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        VaultManager vaultManager = (VaultManager) SingletonBeanRegistry.getBean("VaultManager");
        Map<String, String> secrets = vaultManager.getSecrets();
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                secrets.get("url"), secrets.get("user"), secrets.get("password")
        );
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        poolableConnectionFactory.setValidationQuery("SELECT 1");
        poolableConnectionFactory.setValidationQueryTimeout(3);
        poolableConnectionFactory.setDefaultReadOnly(false);
        poolableConnectionFactory.setDefaultAutoCommit(true);
        poolableConnectionFactory.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return new PoolingDataSource<>(connectionPool);
    }
}
