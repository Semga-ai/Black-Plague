package name.modid.beton.storage.logic.service;

import name.modid.beton.Beton;
import name.modid.beton.storage.logic.manager.GlobalTask;
import name.modid.beton.storage.logic.model.States;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

public class EffectsService {
    private final GlobalTask globalTask;
    private final Map<UUID,Integer> targets;
    private final JavaPlugin plugin;

    public EffectsService(GlobalTask globalTask, Map<UUID, Integer> targets,JavaPlugin plugin) {
        this.globalTask = globalTask;
        this.targets = targets;
        this.plugin = plugin;
    }

    public void iteration() {
        for (UUID uuid : targets.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            if (!targets.containsKey(uuid)) continue;
            int value = targets.get(uuid);
            switch (States.getCurrentState(value)) {
                case NONE -> {
                }
                case FIRST -> {
                    first(player);
                }
                case SECOND -> {
                    second(player);
                }
                case THIRD -> {
                    third(player);
                }
                case FOURTH -> {
                    fourth(player);
                }
            }

        }
    }
    private PotionEffect secondEffect() {
        return new PotionEffect(PotionEffectType.DARKNESS,30,1,false,false,false);
    }
    private PotionEffect thirdEffect() {
        return new PotionEffect(PotionEffectType.SLOWNESS,30,1,false,false,false);
    }

    private PotionEffect fourthEffect() {
        return new PotionEffect(PotionEffectType.WITHER,30,1,false,false,false);
    }


    private void first(Player player) {
        player.getScheduler().run(plugin,task -> {
            player.setFreezeTicks(player.getMaxFreezeTicks());
        },null);
    }

    private void second(Player player) {
        player.getScheduler().run(plugin,task -> {
            player.setFreezeTicks(player.getMaxFreezeTicks());
            player.addPotionEffect(secondEffect());
        },null);
    }

    private void third(Player player) {
        player.getScheduler().run(plugin,task -> {
            player.setFreezeTicks(player.getMaxFreezeTicks());
            player.addPotionEffect(secondEffect());
            player.addPotionEffect(thirdEffect());
        },null);
    }

    private void fourth(Player player) {
        player.getScheduler().run(plugin,task -> {
            player.setFreezeTicks(player.getMaxFreezeTicks());
            player.addPotionEffect(secondEffect());
            player.addPotionEffect(thirdEffect());
            player.addPotionEffect(fourthEffect());
        },null);
    }
}
