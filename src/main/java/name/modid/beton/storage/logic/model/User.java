package name.modid.beton.storage.logic.model;

import org.bukkit.Bukkit;

import java.util.UUID;

//Это на будущее
public record User(UUID uuid) {
    public String getFullName(){
        return Bukkit.getOfflinePlayer(this.uuid).getName();
    }
}
