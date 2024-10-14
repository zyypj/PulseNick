package br.com.pulse.pulsenick.listener;

import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.model.PlayerNick;
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

        // Verifica se o jogador está na database
        if (!nickManager.isPlayerInDatabase(player)) {
            nickManager.registerPlayer(player);
        }

        // Carrega as informações de nick
        PlayerNick playerNick = nickManager.getOrCreateNick(player);

        // Verifica se o jogador usava um nick fake ao sair
        if (playerNick.hasFakeNick()) {
            nickManager.applyNickFake(player, playerNick.getFakeNick());
        }

        // Verifica se o nome real foi alterado
        if (!playerNick.getRealNick().equals(player.getName())) {
            nickManager.resetNickReal(player);
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