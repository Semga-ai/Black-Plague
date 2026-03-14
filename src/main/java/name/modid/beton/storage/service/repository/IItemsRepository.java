package name.modid.beton.storage.service.repository;

import org.bukkit.Material;

import java.util.Set;
import java.util.UUID;

public interface IItemsRepository {
    void connect(String path);
    void save(Material uuid);
    Set<Material> getAllItems();
    boolean contain(Material uuid);
    void remove(Material uuid);
    void close();
}
