package name.modid.beton.storage.service.repository;

import name.modid.beton.storage.service.DAO.ItemsDAO;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ItemRepository implements IItemsRepository {
    private final ItemsDAO dao;
    private volatile Set<Material> data = ConcurrentHashMap.newKeySet();

    public ItemRepository( ItemsDAO d) {
        this.dao = d;
    }

    private void update() {
        this.data = dao.getAll();
    }

    @Override
    public void connect(String path) {
        dao.connect(path);
        dao.createTable();
        update();
        Bukkit.getLogger().info("[DB] Успех!");

    }

    @Override
    public void save(Material uuid) {
        dao.save(uuid);
        data.add(uuid);
    }

    @Override
    public Set<Material> getAllItems() {
        return data;
    }

    @Override
    public boolean contain(Material uuid) {
        return data.contains(uuid);
    }

    @Override
    public void remove(Material uuid) {
        dao.remove(uuid);
        data.remove(uuid);
    }

    @Override
    public void close() {
        dao.close();
    }
}
