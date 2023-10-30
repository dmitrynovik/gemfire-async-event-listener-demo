package main.java.org.example;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;

import java.util.List;
import java.util.Properties;

public class AsyncListenerWriteToPostgres implements AsyncEventListener {

  private LogWriter logger = CacheFactory.getAnyInstance().getLogger();
  private DataSource dataSource;

  @Override
  public void init(Properties props) {
    dataSource = createHikariDataSource();
  }

  @Override
  public void initialize(Cache cache, Properties properties) {
    AsyncEventListener.super.initialize(cache, properties);
    logger = cache.getLogger();
  }

  @Override
  public boolean processEvents(List<AsyncEvent> list) {
    try {
      Connection connection = dataSource.getConnection();
      String sql = "INSERT INTO test(id, name) VALUES (?, ?)";
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      for (AsyncEvent asyncEvent : list) {
        if (asyncEvent.getOperation().isCreate()) {
          preparedStatement.setObject(1, asyncEvent.getKey());
          preparedStatement.setObject(2, asyncEvent.getDeserializedValue());
          preparedStatement.addBatch();
        }
      }
      logger.info("Executing the query");
      // Executing the SQL statement
      preparedStatement.executeUpdate();
      logger.info("Executed the query");
    } catch (SQLException e) {
      logger.error("error executing query: ", e);
      return false;
    }
    return true;
  }

  private static DataSource createHikariDataSource() {
    HikariConfig config = new HikariConfig();

    config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
    config.addDataSourceProperty("serverName", ""); // <--- hostname comes here
    config.addDataSourceProperty("portNumber", "5432");
    config.addDataSourceProperty("databaseName", "testDB");
    config.addDataSourceProperty("user", "postgres");
    config.addDataSourceProperty("password", "postgres@12");

    // postgress configuration for Hikari
    return new HikariDataSource(config);
  }
}