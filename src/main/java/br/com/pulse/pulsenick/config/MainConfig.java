package br.com.pulse.pulsenick.config;

import com.tomkeuper.bedwars.api.configuration.ConfigManager;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import static br.com.pulse.pulsenick.config.ConfigPaths.*;

public class MainConfig extends ConfigManager {
    public MainConfig(Plugin plugin, String name, String dir) {
        super(plugin, name, dir);
        YamlConfiguration yml = getYml();
        yml.options().header("PulseNick By tadeu (@zypj)");
        yml.addDefault(NICK_PERMISSION, "nick.use");
        yml.options().copyDefaults(true);
        save();
    }
}
