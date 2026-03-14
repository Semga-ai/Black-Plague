package name.modid.beton.storage.logic.service;

import name.modid.beton.storage.logic.manager.GlobalTask;
import name.modid.beton.storage.logic.utils.Check;
import name.modid.beton.storage.service.repository.IUserRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class CounterService {
    private GlobalTask globalTask;
    private Map<UUID,Integer> targets;
    private final JavaPlugin plugin;
    private final IUserRepository userRepository;
    public CounterService(Map<UUID, Integer> targets, GlobalTask globalTask, JavaPlugin plugin, IUserRepository iUserRepository) {
        this.targets = targets;
        this.globalTask = globalTask;
        this.plugin = plugin;
        this.userRepository = iUserRepository;
    }

    public void iteration() {
        for (UUID uuid : targets.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            Integer val = targets.getOrDefault(uuid,0);
            if (val == null) continue;
            if (player == null) continue;
            if (userRepository.contain(uuid)) continue;
            player.getScheduler().run(plugin,task -> {
                if (Check.check(player.getLocation().add(0,-1,0))) {
                    targets.put(uuid,val + 1);
                } else {
                    if (val > 0) {
                        targets.put(uuid,val - 1);
                    }
                }
            },null);
        }
    }
}
