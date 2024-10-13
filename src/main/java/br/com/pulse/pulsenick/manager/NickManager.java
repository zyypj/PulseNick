package br.com.pulse.pulsenick.manager;

import br.com.pulse.pulsenick.storage.SQLite;
import org.bukkit.entity.Player;

public class NickManager {

    private final SQLite sqLite;

    public NickManager(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    public void applyNickFake(Player player, String nickFake) {
        String playerUUID = String.valueOf(player.getUniqueId());
        sqLite.savePlayer(playerUUID, nickFake);
    }

    public void applyNickReal(Player player, String nickReal) {
        String playerUUID = String.valueOf(player.getUniqueId());
        sqLite.resetPlayer(playerUUID, nickReal);
    }

    public String getNickFake(Player player) {
        String playerUUID = String.valueOf(player.getUniqueId());
        return sqLite.getNickFake(playerUUID);
    }

    public String getNickReal(Player player) {
        String playerUUID = String.valueOf(player.getUniqueId());
        return sqLite.getNickReal(playerUUID);
    }
}
