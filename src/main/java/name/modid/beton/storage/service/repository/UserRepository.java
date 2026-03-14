package name.modid.beton.storage.service.repository;

import name.modid.beton.storage.logic.model.User;
import name.modid.beton.storage.service.DAO.SqliteDAO;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Contract;

import java.sql.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository implements IUserRepository{
    private final SqliteDAO dao;
    //готовые запросы
    private String sqlGetAllUUID = "SELECT uuid FROM player";
    private String sqlWriteUUID = "INSERT OR IGNORE INTO player (uuid) VALUES (?);";
    private String sqlDeleteUUID = "DELETE FROM player WHERE uuid = ?;";
    //кеш
    private volatile Map<UUID,User> data = new ConcurrentHashMap<>();

    public UserRepository(SqliteDAO d) {
        this.dao = d;
    }



    @Contract
    private void update() {
        this.data = dao.getAll();
    }



    //реализация интерфейса >u<
    @Override
    public void connect(String path) {
        dao.connect(path);
        dao.createTable();
        update();

        Bukkit.getLogger().info("[BD] Успех!");
    }

    @Override
    public User getUser(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public void save(UUID uuid) {
        dao.save(uuid);
        data.put(uuid,new User(uuid));
        Bukkit.getLogger().info("[DB] Успешно добавлен");
    }

    @Override
    public Set<UUID> getAllUsers() {
        return data.keySet();
    }

    @Override
    public boolean contain(UUID uuid) {
        return data.containsKey(uuid);
    }

    @Override
    public void remove(UUID uuid) {
        dao.remove(uuid);
        data.remove(uuid);
        Bukkit.getLogger().info("[DB] Успешно удален");
    }

    @Override
    public void close() {
        dao.close();
    }


}
