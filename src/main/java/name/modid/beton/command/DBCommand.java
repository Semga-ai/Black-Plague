package name.modid.beton.command;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import name.modid.beton.storage.service.repository.IItemsRepository;
import name.modid.beton.storage.service.repository.IUserRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;
import org.incendo.cloud.annotations.suggestion.Suggestions;
import org.incendo.cloud.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DBCommand {
    private final IUserRepository repository;
    private final IItemsRepository itemsRepository;
    public DBCommand(IUserRepository i, IItemsRepository ii) {
        this.repository = i;
        this.itemsRepository = ii;
    }

    @Command("BQ addUser <name>")
    @Permission("QB.admin")
    public void add(
            final @NotNull CommandSourceStack sender,
            final @Argument(value = "name",suggestions = "add_data") String name
            ) {
        UUID id = Bukkit.getOfflinePlayer(name).getUniqueId();
        if (repository.contain(id)) {
            sender.getSender().sendMessage(Component.text("[DB] Вы уже добавили игрока ")
                    .append(Component.text(name))
                    .append(Component.text(" в список!"))
                    .color(NamedTextColor.YELLOW)
            );
            return;
        }
        repository.save(id);
    }

    @Command("BQ addItem <name>")
    @Permission("QB.admin")
    public void addItem(
            final @NotNull CommandSourceStack sender,
            final @Argument(value = "name",suggestions = "item") Material name
    ) {
        if (itemsRepository.contain(name)) {
            sender.getSender().sendMessage(Component.text("[DB] Вы уже добавили предмет ")
                    .append(Component.text(name.toString()))
                    .append(Component.text(" в список!"))
                    .color(NamedTextColor.YELLOW)
            );
            return;
        }
        itemsRepository.save(name);
    }



    @Command("BQ remUser <name>")
    @Permission("QB.admin")
    public void rem(
            final @NotNull CommandSourceStack sender,
            final @Argument(value = "name",suggestions = "rem_data") String name
    ) {
        UUID id = Bukkit.getOfflinePlayer(name).getUniqueId();
        if (!repository.contain(id)) {
            sender.getSender().sendMessage(Component.text("[DB] Игрока ")
                    .append(Component.text(name))
                    .append(Component.text(" уже нет в списке!"))
                    .color(NamedTextColor.YELLOW)
            );
            return;
        }
        repository.remove(id);
    }

    @Command("BQ remItem <name>")
    @Permission("QB.admin")
    public void remItem(
            final @NotNull CommandSourceStack sender,
            final @Argument(value = "name",suggestions = "item") Material name
    ) {
        if (!itemsRepository.contain(name)) {
            sender.getSender().sendMessage(Component.text("[DB] Предмета ")
                    .append(Component.text(name.toString()))
                    .append(Component.text(" уже нет в списке!"))
                    .color(NamedTextColor.YELLOW)
            );
            return;
        }
        itemsRepository.remove(name);
    }





    @Suggestions("add_data")
    public List<String> suggestions(CommandContext<CommandSourceStack> context, String input) {
        String filter = (input == null) ? "" : input.toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getName().toLowerCase().startsWith(filter))
                .map(Player::getName)
                .toList();
    }

    @Suggestions("rem_data")
    public List<String> suggestionss(CommandContext<CommandSourceStack> context, String input) {
        return repository.getAllUsers().stream().map(t -> repository.getUser(t).getFullName()).toList();
    }

    @Suggestions("item")
    public List<String> suggestionsss(CommandContext<CommandSourceStack> context, String input) {
        return Arrays.stream(Material.values())
                .filter(Material::isItem)
                .filter(m -> !m.isAir())
                .map(t -> t.name())
                .collect(Collectors.toList());
    }
}
