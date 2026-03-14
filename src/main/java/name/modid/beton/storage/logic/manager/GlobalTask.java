package name.modid.beton.storage.logic.manager;


import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import name.modid.beton.storage.logic.Services;
import name.modid.beton.storage.service.repository.IItemsRepository;
import name.modid.beton.storage.service.repository.IUserRepository;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class GlobalTask {
    private JavaPlugin plugin;
    private IUserRepository userRepository;
    private ScheduledTask task;
    private Map<UUID,Integer> targets = new ConcurrentHashMap<>();
    private IItemsRepository itemsRepository;
    private final Services services;
    public GlobalTask(JavaPlugin plugin, IUserRepository userRepository,IItemsRepository iItemsRepository) {
        this.plugin = plugin;
        this.itemsRepository = iItemsRepository;
        this.userRepository = userRepository;
        this.services = new Services(plugin,userRepository,this,targets,itemsRepository);
    }

    public void start() {
        this.task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin,(task) -> {
            services.findTargetsService.iteration();
            services.counterService.iteration();
            services.effectsService.iteration();
            services.itemService.iteration();
        },1l,20l);
    }

    public void add(UUID uuid) {
        targets.put(uuid,targets.getOrDefault(uuid,0));
    }
    public void remove(UUID uuid) {
        Integer val = targets.get(uuid);
        if (val == null) return;
        if (val != 0) return;
        targets.remove(uuid);
    }
}
