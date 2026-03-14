package name.modid.beton.storage.logic.service;

import name.modid.beton.storage.logic.utils.Check;
import name.modid.beton.storage.service.repository.IItemsRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

public class ItemService {
    private final JavaPlugin plugin;
    private final IItemsRepository itemsRepository;

    public ItemService(JavaPlugin plugin, IItemsRepository itemsRepository) {
        this.plugin = plugin;
        this.itemsRepository = itemsRepository;
    }


    public void iteration() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().run(plugin,task -> {
                Collection<Item> near = player.getWorld().getNearbyEntitiesByType(Item.class,player.getLocation(),10,10,10);
                for (Item item : near) {
                    if (itemsRepository.contain(item.getItemStack().getType())) {
                        item.getScheduler().run(plugin,taskk -> {
                            if (Check.check(item.getLocation().clone().add(0,-1,0))) {
                                item.remove();
                            }
                        },null);
                    }
                }
            },null);
        }
    }
}
