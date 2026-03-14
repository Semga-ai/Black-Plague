package name.modid.beton.storage.logic.service;

import name.modid.beton.storage.logic.manager.GlobalTask;
import name.modid.beton.storage.logic.utils.Check;
import name.modid.beton.storage.service.repository.IUserRepository;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FindTargetsService {
    private final JavaPlugin plugin;
    private final IUserRepository repository;
    private final GlobalTask globalTask;

    public FindTargetsService(JavaPlugin plugin, IUserRepository iUserRepository,GlobalTask g) {
        this.plugin = plugin;
        this.repository = iUserRepository;
        this.globalTask = g;
    }

    public void iteration() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getScheduler().run(plugin,task -> {
                if (repository.contain(player.getUniqueId())) {
                    task.cancel();
                    return;
                }
                if (!Check.check(player.getLocation().clone().add(0,-1,0))) {
                    globalTask.remove(player.getUniqueId());
                    task.cancel();
                    return;
                }
                globalTask.add(player.getUniqueId());
            },null);
        }
    }
}
