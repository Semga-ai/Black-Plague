package name.modid.beton.storage.service.DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import name.modid.beton.storage.logic.model.User;
import org.bukkit.Material;

import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ItemsDAO {
    private HikariDataSource dataSource;

    // готовые запросы
    private final String sqlGetAllUUID = "SELECT material FROM item";
    private final String sqlWriteUUID = "INSERT OR IGNORE INTO item (material) VALUES (?);";
    private final String sqlDeleteUUID = "DELETE FROM item WHERE material = ?;";

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
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS item (material TEXT PRIMARY KEY);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<Material> getAll() {
        Set<Material> m = ConcurrentHashMap.newKeySet();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetAllUUID);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Material id = Material.valueOf(rs.getString("material"));
                m.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }

    public CompletableFuture<Void> remove(Material uuid) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sqlDeleteUUID)) {
                ps.setString(1, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> save(Material uuid) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(sqlWriteUUID)) {
                ps.setString(1, uuid.toString());
                ps.executeUpdate();
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
