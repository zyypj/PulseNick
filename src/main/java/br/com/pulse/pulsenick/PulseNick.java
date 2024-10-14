package br.com.pulse.pulsenick;

import br.com.pulse.pulsenick.cache.NickCache;
import br.com.pulse.pulsenick.command.NickCommand;
import br.com.pulse.pulsenick.config.MainConfig;
import br.com.pulse.pulsenick.config.MessagesData;
import br.com.pulse.pulsenick.database.NickRepository;
import br.com.pulse.pulsenick.hook.PAPIExpansion;
import br.com.pulse.pulsenick.listener.PlayerListener;
import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.util.Utility;
import com.tomkeuper.bedwars.api.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PulseNick extends JavaPlugin {

    private NickRepository nickRepository;
    private NickManager nickManager;
    private Utility utility;
    private Connection connection;
    private BedWars bw2023Api;
    private NickCache nickCache;

    /**
     * Singleton seguro e eficiente usando o Holder Pattern.
     * Secure and efficient Singleton using the Holder Pattern.
     */
    private static class InstanceHolder {
        private static final PulseNick INSTANCE = new PulseNick();
    }

    /**
     * Retorna a instância do plugin usando o Holder Pattern.
     * Returns the plugin instance using the Holder Pattern.
     */
    public static PulseNick getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * Método chamado quando o plugin é habilitado.
     * Método responsável por carregar suporte, conectar ao banco de dados, iniciar configurações, e registrar comandos e eventos.
     * <p>
     * Method called when the plugin is enabled.
     * Responsible for loading support, connecting to the database, start configs, and registering commands and events.
     */
    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        try {
            // Carrega o suporte necessário e inicializa dependências
            // Loads necessary support and initializes dependencies
            loadSupport();
            utility = new Utility(bw2023Api);

            // Conectar ao banco de dados
            // Connect to the database
            connectDatabase();

            // Iniciar o cache e o gerenciador de nicks
            // Start the cache and nick manager
            nickCache = new NickCache();
            nickManager = new NickManager(nickRepository, nickCache);

            // Iniciar a config.yml e mensagens
            // Start the config.yml and messages
            loadConfig();

            // Registrar eventos e comandos
            // Register events and commands
            registerListeners();
            registerCommands();

            // Registrar placeholders
            // Register placeholders
            registerPlaceholders();

        } catch (Exception e) {
            // Em caso de qualquer falha, desabilitar o plugin e logar o erro
            // In case of any failure, disable the plugin and log the error
            getLogger().severe("An error occurred during plugin startup: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        long endTime = System.currentTimeMillis();
        getLogger().info("Plugin enabled in " + (endTime - startTime) + "ms");
    }

    /**
     * Método chamado quando o plugin é desabilitado.
     * Fecha a conexão com o banco de dados de forma segura.
     * <p>
     * Method called when the plugin is disabled.
     * Closes the database connection safely.
     */
    @Override
    public void onDisable() {
        long startTime = System.currentTimeMillis();

        // Fechar conexão de forma segura
        // Safely close the connection
        if (connection != null) {
            try {
                connection.close();
                getLogger().info("Database connection closed successfully.");
            } catch (SQLException e) {
                getLogger().severe("Error closing database connection: " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        getLogger().info("Plugin disabled in " + (endTime - startTime) + "ms");
    }
    /**
     * Carrega o suporte necessário para o plugin BedWars2023.
     * Verifica se o plugin está presente e habilita o suporte.
     * <p>
     * Loads the necessary support for the BedWars2023 plugin.
     * Checks if the plugin is present and enables support.
     */
    private void loadSupport() {
        PluginManager pm = Bukkit.getPluginManager();

        if (pm.getPlugin("BedWars2023") == null) {
            getLogger().severe("BedWars2023 not found. Disabling plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        bw2023Api = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        getLogger().info("Support for BedWars2023 enabled.");
    }
    /**
     * Conecta ao banco de dados SQLite e cria as tabelas necessárias.
     * Se a conexão falhar, o plugin é desabilitado.
     * <p>
     * Connects to the SQLite database and creates the necessary tables.
     * If the connection fails, the plugin is disabled.
     */
    private void connectDatabase() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Connecting to the database...");

        try {
            File addonPath = new File(bw2023Api.getAddonsPath().getPath() + File.separator + "PulseNick");
            if (!addonPath.exists()) {
                addonPath.mkdirs();
            }

            File databaseFile = new File(addonPath, "storage.db");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getPath());

            // Agora dentro do bloco try, garantimos que SQLException pode ser lançada
            // Now within the try block, we ensure that SQLException can be thrown
            nickRepository = new NickRepository(connection);
            nickRepository.createTables();

            long endTime = System.currentTimeMillis();
            getLogger().info("Database connection established in " + (endTime - startTime) + "ms");

        } catch (SQLException e) {
            getLogger().severe("Could not connect to database: " + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    /**
     * Inicializa a config e as mensagens do plugin, as criando se necessário.
     * <p>
     * Initializes the plugin's config and messages, creating them if necessary.
     */
    private void loadConfig() {

        new MainConfig(this, "config", bw2023Api.getAddonsPath().getPath() + File.separator + "PulseNick");

        new MessagesData();

    }
    /**
     * Registra os listeners de eventos para o plugin.
     * <p>
     * Registers event listeners for the plugin.
     */
    private void registerListeners() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Registering listeners...");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(nickManager), this);

        long endTime = System.currentTimeMillis();
        getLogger().info("Listeners registered in " + (endTime - startTime) + "ms");
    }
    /**
     * Registra os comandos para o plugin.
     * <p>
     * Registers commands for the plugin.
     */
    private void registerCommands() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Registering commands...");

        getCommand("nick").setExecutor(new NickCommand(nickManager, utility));

        long endTime = System.currentTimeMillis();
        getLogger().info("Commands registered in " + (endTime - startTime) + "ms");
    }
    /**
     * Registra a expansão do PlaceholderAPI se o plugin estiver presente.
     * <p>
     * Registers the PlaceholderAPI expansion if the plugin is present.
     */
    private void registerPlaceholders() {
        Plugin papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (papi != null) {
            new PAPIExpansion(nickCache, nickRepository).register();
            getLogger().info("PlaceholderAPI expansion registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Expansion not registered.");
        }
    }
}