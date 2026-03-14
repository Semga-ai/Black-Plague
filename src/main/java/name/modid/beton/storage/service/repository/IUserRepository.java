package name.modid.beton.storage.service.repository;

import name.modid.beton.storage.logic.model.User;

import java.util.Set;
import java.util.UUID;


public interface IUserRepository {
    void connect(String path);
    User getUser(UUID uuid);
    void save(UUID uuid);
    Set<UUID> getAllUsers();
    boolean contain(UUID uuid);
    void remove(UUID uuid);
    void close();
}
