package name.modid.beton;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import name.modid.beton.command.DBCommand;
import name.modid.beton.storage.logic.manager.GlobalTask;
import name.modid.beton.storage.service.DAO.ItemsDAO;
import name.modid.beton.storage.service.DAO.SqliteDAO;
import name.modid.beton.storage.service.repository.IItemsRepository;
import name.modid.beton.storage.service.repository.IUserRepository;
import name.modid.beton.storage.service.repository.ItemRepository;
import name.modid.beton.storage.service.repository.UserRepository;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.meta.CommandMeta;
import org.incendo.cloud.paper.PaperCommandManager;

public final class Beton extends JavaPlugin {
    private PaperCommandManager commandManager;
    private IUserRepository userRepository;
    private IItemsRepository itemsRepository;
    private SqliteDAO sqliteDAO;
    private ItemsDAO itemsDAO;
    private GlobalTask globalTask;
    @Override
    public void onEnable() {
        sqliteDAO = new SqliteDAO();
        itemsDAO = new ItemsDAO();
        userRepository = new UserRepository(this.sqliteDAO);
        itemsRepository = new ItemRepository(this.itemsDAO);

        //папочки
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        userRepository.connect(getDataFolder() + "/dbUsers.db");
        itemsRepository.connect(getDataFolder() + "/dbItems.db");

        //менеджеры
        this.globalTask = new GlobalTask(this,userRepository,itemsRepository);
        globalTask.start();



        //Инициализируем Cloud
        this.commandManager = PaperCommandManager.builder(SenderMapper.identity())
                .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
                .buildOnEnable(this);


        //И парсер тоже
        AnnotationParser<CommandSourceStack> annotationParser = new AnnotationParser<>(
                this.commandManager,
                CommandSourceStack.class,
                params -> CommandMeta.empty()
        );


        //Регистрация команд
        try {
            annotationParser.parse(new DBCommand(userRepository,itemsRepository));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        userRepository.close();
        itemsRepository.close();
    }

    public IUserRepository getUserRepository() {return userRepository;}
}
