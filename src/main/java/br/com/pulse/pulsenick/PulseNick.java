package br.com.pulse.pulsenick;

import br.com.pulse.pulsenick.command.NickCommand;
import br.com.pulse.pulsenick.config.MainConfig;
import br.com.pulse.pulsenick.config.MessagesData;
import br.com.pulse.pulsenick.hook.PlaceholderAPI;
import br.com.pulse.pulsenick.listener.PlayerListener;
import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.storage.SQLite;
import br.com.pulse.pulsenick.util.Utility;
import com.tomkeuper.bedwars.api.BedWars;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class PulseNick extends JavaPlugin {

    private static PulseNick pulseNick;
    private SQLite sqLite;
    private NickManager nickManager;
    private BedWars bw2023Api;

    @Override
    public void onEnable() {

        pulseNick = this;

        loadSupport();
        loadConfig();
        connectDatabase();
        loadManager();
        registerListener();
        registerCommand();
        registerPlaceholders();

    }

    @Override
    public void onDisable() {

        closeDatabase();

    }

    private void loadSupport() {

        PluginManager pm = Bukkit.getPluginManager();

        if (pm.getPlugin("BedWars2023") == null) {
            Utility.warn("BedWars not founding...");
            Utility.warn("Disabling plugin.");
            getPluginLoader().disablePlugin(this);
        }
        bw2023Api = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
    }
    private void loadConfig() {

        long startTime = System.currentTimeMillis();

        Utility.info("&eLoading configs/messages...");

        new MainConfig(this, "config", bw2023Api.getAddonsPath().getPath() + File.separator + "PulseNick");

        new MessagesData();

        long endTime = System.currentTimeMillis();

        Utility.info("&aConfigs/Messages loaded. " + (endTime - startTime) + "ms");
    }
    private void connectDatabase() {

        long startTime = System.currentTimeMillis();

        Utility.info("&eConnecting to database...");

        sqLite = new SQLite();

        sqLite.connect();
        sqLite.createDatabase();

        long endTime = System.currentTimeMillis();

        Utility.info("&aDatabase connected. " + (endTime - startTime) + "ms");
    }
    private void closeDatabase() {

        Utility.info("&eClosing database...");

        if (sqLite != null) {
            sqLite.closeConnection();
        }

        Utility.info("&aDatabase closed.");
    }
    private void loadManager() {

        long startTime = System.currentTimeMillis();

        Utility.info("&eLoading manager...");

        nickManager = new NickManager(sqLite);

        long endTime = System.currentTimeMillis();

        Utility.info("&aManager loaded. " + (endTime - startTime) + "ms");
    }
    private void registerListener() {

        long startTime = System.currentTimeMillis();

        Utility.info("&eRegistering listener...");

        Bukkit.getPluginManager().registerEvents(new PlayerListener(nickManager), this);

        long endTime = System.currentTimeMillis();

        Utility.info("&aListener registered. " + (endTime - startTime) + "ms");

    }
    private void registerCommand() {

        long startTime = System.currentTimeMillis();

        Utility.info("&eRegistering command...");

        getCommand("nick").setExecutor(new NickCommand(nickManager));

        long endTime = System.currentTimeMillis();

        Utility.info("&aCommand registered. " + (endTime - startTime) + "ms");

    }
    private void registerPlaceholders() {

        new PlaceholderAPI().register();

    }

    public static PulseNick getInstance() {
        return pulseNick;
    }
}
