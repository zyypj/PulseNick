package br.com.pulse.pulsenick.listener;

import br.com.pulse.pulsenick.manager.NickManager;
import dev.iiahmed.disguise.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final NickManager nickManager;
    private final DisguiseProvider disguiseProvider;

    public PlayerListener(NickManager nickManager) {
        this.nickManager = nickManager;
        this.disguiseProvider = DisguiseManager.getProvider();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // Verifica se o jogador está no banco de dados
        if (!nickManager.isPlayerInDatabase(player)) {
            // Salva o jogador no banco de dados com nickFake como null e nickReal como o nome atual
            nickManager.applyNickReal(player, player.getName());
            nickManager.applyNickFake(player, null);  // nickFake como null
        }

        // Verifica se o jogador tinha um nickFake salvo
        String nickFake = nickManager.getNickFake(player);

        if (nickFake != null && !nickFake.isEmpty()) {
            // Aplica o nickFake ao jogador
            Disguise disguise = Disguise.builder()
                    .setName(nickFake)
                    .setSkin(player.getUniqueId()) // Usa o UUID diretamente
                    .build();
            disguiseProvider.disguise(player, disguise);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        // Obtém as informações do jogador disfarçado
        PlayerInfo info = disguiseProvider.getInfo(player);

        if (info.hasName()) {
            nickManager.applyNickFake(player, info.getNickname());
        }
    }
}
