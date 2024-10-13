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

    private final BedWars bwAPI;

    public Utility(BedWars bwAPI) {
        this.bwAPI = bwAPI;
    }

    public void info(String text) {
        Bukkit.getConsoleSender().sendMessage("[" + "PulseNick" + "] " + c(text));
    }

    public void warn(String text) {
        PulseNick.getInstance().getLogger().warning(c(text));
    }

    public String c(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String p(Player player, String text) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return c(text);
        return c(PlaceholderAPI.setPlaceholders(player, text));
    }

    public String getMsg(Player player, String path) {
        return p(player, bwAPI.getPlayerLanguage(player).getString(path));
    }

    public List<String> getListMsg(Player player, String path) {
        return bwAPI.getPlayerLanguage(player).getList(path).stream().map(s -> p(player, s)).collect(Collectors.toList());
    }
}