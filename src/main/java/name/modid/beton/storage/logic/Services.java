package name.modid.beton.storage.logic;

import name.modid.beton.storage.logic.manager.GlobalTask;
import name.modid.beton.storage.logic.service.CounterService;
import name.modid.beton.storage.logic.service.EffectsService;
import name.modid.beton.storage.logic.service.FindTargetsService;
import name.modid.beton.storage.logic.service.ItemService;
import name.modid.beton.storage.service.repository.IItemsRepository;
import name.modid.beton.storage.service.repository.IUserRepository;
import name.modid.beton.storage.service.repository.UserRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public class Services {
    private final JavaPlugin plugin;
    private final IUserRepository userRepository;
    private final GlobalTask globalTask;
    private final Map<UUID,Integer> targets;
    public final FindTargetsService findTargetsService;
    public final CounterService counterService;
    public final EffectsService effectsService;
    public final ItemService itemService;
    public final IItemsRepository itemsRepository;
    public Services(JavaPlugin plugin, IUserRepository iUserRepository, GlobalTask task, Map<UUID,Integer> targ, IItemsRepository itemsRepository) {
        this.plugin = plugin;
        this.userRepository = iUserRepository;
        this.globalTask = task;
        this.targets = targ;
        this.itemsRepository = itemsRepository;
        this.itemService = new ItemService(plugin,itemsRepository);
        this.findTargetsService = new FindTargetsService(plugin,userRepository,globalTask);
        this.counterService = new CounterService(targets,globalTask,plugin,userRepository);
        this.effectsService = new EffectsService(globalTask,targets,plugin);
    }

}
