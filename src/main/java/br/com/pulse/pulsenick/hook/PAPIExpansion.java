package br.com.pulse.pulsenick.hook;

import dev.iiahmed.disguise.DisguiseManager;
import dev.iiahmed.disguise.DisguiseProvider;
import dev.iiahmed.disguise.PlayerInfo;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIExpansion extends PlaceholderExpansion {

    private final DisguiseProvider provider = DisguiseManager.getProvider();

    @Override
    public @NotNull String getIdentifier() {
        return "pulsenick";
    }

    @Override
    public @NotNull String getAuthor() {
        return "tadeu";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(final Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        /*
         * 
         *
         */

        String request = params.toLowerCase();
        PlayerInfo info = provider.getInfo(player);
        return switch (request) {
            case "name" -> info.getNickname();
            case "realname" -> info.getName();
            case "is_nicked" -> String.valueOf(provider.isDisguised(player));
            default -> null;
        };
    }
}
