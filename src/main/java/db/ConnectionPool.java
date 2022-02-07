package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

import static db.ConnectionFactory.createConnection;

public class ConnectionPool {

    private static final Logger log = LoggerFactory.getLogger(ConnectionPool.class);

    private static final int MAX_POOL_SIZE = 10;
    private static final int INITIAL_POOL_SIZE = 3;

    private final ConcurrentLinkedDeque<Connection> pool;
    private final AtomicInteger currentUsedSize;

    public static ConnectionPool create(String url, String user, String password) throws SQLException {
        ConcurrentLinkedDeque<Connection> pool = new ConcurrentLinkedDeque<>();
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.push(createConnection(url, user, password));
        }
        return new ConnectionPool(pool);
    }

    public ConnectionPool(ConcurrentLinkedDeque<Connection> pool) {
        this.pool = pool;
        this.currentUsedSize = new AtomicInteger();
    }

    private ConnectionPool() {
        throw new UnsupportedOperationException();
    }

    public void retrieve(Connection connection) {
        int size = currentUsedSize.decrementAndGet();
        log.debug("Current connection pool size : {}", size);
        pool.push(connection);
    }

    public Connection getConnection(String url, String user, String password) throws SQLException {
        if (pool.isEmpty() && currentUsedSize.get() < MAX_POOL_SIZE) {
            pool.push(createConnection(url, user, password));
        }
        try {
            Connection conn = pool.pop();
            int size = currentUsedSize.incrementAndGet();
            log.debug("Current connection pool size : {}", size);
            return conn;
        } catch (NoSuchElementException e) {
            String errorMessage = "Maximum pool size reached, no available connections!";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }


}
