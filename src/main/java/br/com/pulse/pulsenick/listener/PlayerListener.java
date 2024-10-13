package br.com.pulse.pulsenick.listener;

import br.com.pulse.pulsenick.manager.NickManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NickManager nickManager;

    public PlayerListener(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Verifica se o jogador está na database, se não, registra
        if (!nickManager.isPlayerInDatabase(player)) {
            nickManager.resetNickReal(player); // Inicializa com o nome real
        }

        // Carrega o nick do jogador, aplica o fake se existir
        if (nickManager.hasNickFake(player)) {
            nickManager.applyNickFake(player, nickManager.getNickFake(player));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Verifica se o jogador está usando um nick fake e salva no banco de dados
        if (nickManager.hasNickFake(player)) {
            nickManager.applyNickFake(player, nickManager.getNickFake(player));
        } else {
            nickManager.resetNickReal(player);
        }
    }
}