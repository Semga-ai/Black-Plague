package name.modid.beton.storage.service.DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import name.modid.beton.storage.logic.model.User;
import org.jetbrains.annotations.Contract;

import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class SqliteDAO {
    private HikariDataSource dataSource;


    //готовые запросы
    private String sqlGetAllUUID = "SELECT uuid FROM player";
    private String sqlWriteUUID = "INSERT OR IGNORE INTO player (uuid) VALUES (?);";
    private String sqlDeleteUUID = "DELETE FROM player WHERE uuid = ?;";





    @Contract("null -> fail")
    public void connect(String path) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + path);
            config.setPoolName("SQLitePool");
            config.setMaximumPoolSize(1);
            config.addDataSourceProperty("journal_mode", "WAL");
            config.addDataSourceProperty("synchronous", "NORMAL");
            this.dataSource = new HikariDataSource(config);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
        try(Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement();) {
            statement.execute("""
            CREATE TABLE IF NOT EXISTS player (
              uuid TEXT PRIMARY KEY
            );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID,User> getAll() {
        Map<UUID,User> m = new ConcurrentHashMap<>();
        try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sqlGetAllUUID); ResultSet rs = statement.executeQuery();) {
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("uuid"));
                m.put(id,new User(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }


    public CompletableFuture<Void> remove(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUUID);
            ) {
                preparedStatement.setString(1,uuid.toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> save(UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            try(Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sqlWriteUUID)) {
                statement.setString(1,uuid.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }
}
