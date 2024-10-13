package br.com.pulse.pulsenick.listener;

import br.com.pulse.pulsenick.manager.NickManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private final NickManager nickManager;

    public PlayerListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

    }
}
