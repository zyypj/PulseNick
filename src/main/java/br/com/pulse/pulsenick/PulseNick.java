package br.com.pulse.pulsenick;

import br.com.pulse.pulsenick.cache.NickCache;
import br.com.pulse.pulsenick.command.NickCommand;
import br.com.pulse.pulsenick.database.NickRepository;
import br.com.pulse.pulsenick.listener.PlayerListener;
import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.util.Utility;
import com.tomkeuper.bedwars.api.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class PulseNick extends JavaPlugin {

    private static PulseNick instance;
    private NickRepository nickRepository;
    private NickManager nickManager;
    private Utility utility;
    private Connection connection;
    private BedWars bw2023Api;
    private NickCache nickCache;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();
        instance = this;

        // Carregando dependências necessárias
        loadSupport();

        // Iniciando utilitário
        utility = new Utility(bw2023Api);

        // Conectando ao banco de dados
        try {
            connectDatabase();
            nickCache = new NickCache();
        } catch (SQLException e) {
            getLogger().severe("Could not connect to database. Disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Inicializando o gerenciador de nicks com cache
        nickManager = new NickManager(nickRepository, nickCache);

        // Registrando eventos e comandos
        registerListeners();
        registerCommands();

        long endTime = System.currentTimeMillis();
        getLogger().info("Plugin habilitado em " + (endTime - startTime) + "ms");
    }

    @Override
    public void onDisable() {
        long startTime = System.currentTimeMillis();

        // Fechando a conexão com o banco de dados de forma segura
        if (connection != null) {
            try {
                connection.close();
                getLogger().info("Database connection closed successfully.");
            } catch (SQLException e) {
                getLogger().severe("Error closing database connection: " + e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        getLogger().info("Plugin desabilitado em " + (endTime - startTime) + "ms");
    }

    /**
     * Carrega o suporte ao plugin BedWars2023 e verifica se está presente
     */
    private void loadSupport() {
        PluginManager pm = Bukkit.getPluginManager();

        if (pm.getPlugin("BedWars2023") == null) {
            getLogger().severe("BedWars2023 não encontrado. Desabilitando plugin.");
            getPluginLoader().disablePlugin(this);
            return;
        }

        bw2023Api = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        getLogger().info("Suporte ao BedWars2023 habilitado.");
    }
    /**
     * Conecta ao banco de dados SQLite e cria as tabelas necessárias
     */
    private void connectDatabase() throws SQLException {
        long startTime = System.currentTimeMillis();
        getLogger().info("Conectando ao banco de dados...");

        connection = DriverManager.getConnection("jdbc:sqlite:plugins/PulseNick/storage.db");
        nickRepository = new NickRepository(connection);
        nickRepository.createTables();

        long endTime = System.currentTimeMillis();
        getLogger().info("Conexão com o banco de dados estabelecida em " + (endTime - startTime) + "ms");
    }
    /**
     * Registra os listeners de eventos
     */
    private void registerListeners() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Registrando listeners...");

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(nickManager), this);

        long endTime = System.currentTimeMillis();
        getLogger().info("Listeners registrados em " + (endTime - startTime) + "ms");
    }
    /**
     * Registra os comandos do plugin
     */
    private void registerCommands() {
        long startTime = System.currentTimeMillis();
        getLogger().info("Registrando comandos...");

        getCommand("nick").setExecutor(new NickCommand(nickManager, utility));

        long endTime = System.currentTimeMillis();
        getLogger().info("Comandos registrados em " + (endTime - startTime) + "ms");
    }
    /**
     * Retorna a instância singleton do plugin
     */
    public static PulseNick getInstance() {
        return instance;
    }
}