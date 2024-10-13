package br.com.pulse.pulsenick.util;

import br.com.pulse.pulsenick.PulseNick;
import com.tomkeuper.bedwars.api.BedWars;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class Utility {

    static BedWars bwAPI = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();

    public static void info(String text) {
        Bukkit.getConsoleSender().sendMessage("[" + PulseNick.getInstance().getName() + "] " + c(text));
    }

    public static void warn(String text) {
        PulseNick.getInstance().getLogger().warning(c(text));
    }

    public static String c(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static String p(Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return c(text);
        return c(PlaceholderAPI.setPlaceholders(player, text));
    }

    public static String getMsg(Player player, String path) {
        return p(player, bwAPI.getPlayerLanguage(player).getString(path));
    }

    public static List<String> getListMsg(Player player, String path) {
        return bwAPI.getPlayerLanguage(player).getList(path).stream().map(s -> p(player, s)).collect(Collectors.toList());
    }
}
