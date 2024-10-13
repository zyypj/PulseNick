package br.com.pulse.pulsenick.manager;

import br.com.pulse.pulsenick.cache.NickCache;
import br.com.pulse.pulsenick.database.NickRepository;
import br.com.pulse.pulsenick.model.PlayerNick;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NickManager {

    private final NickRepository repository;
    private final NickCache cache;

    public NickManager(NickRepository repository, NickCache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    // Verifica se o jogador está na base de dados
    public boolean isPlayerInDatabase(Player player) {
        return repository.isPlayerInDatabase(player.getUniqueId());
    }

    // Obtém ou cria as informações de nick de um jogador
    public PlayerNick getOrCreateNick(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (cache.hasNick(player)) {
            return cache.getNick(player);
        }

        String nickFake = repository.getNickFake(playerUUID);
        if (nickFake == null) {
            return new PlayerNick(player.getName(), null);
        }

        PlayerNick playerNick = new PlayerNick(player.getName(), nickFake);
        cache.setNick(player, playerNick);
        return playerNick;
    }

    // Aplica o nick fake e salva no banco de dados e cache
    public void applyNickFake(Player player, String nickFake) {
        UUID playerUUID = player.getUniqueId();
        repository.savePlayer(playerUUID, nickFake);

        PlayerNick playerNick = new PlayerNick(player.getName(), nickFake);
        cache.setNick(player, playerNick);
    }

    // Reseta o nick real do jogador
    public void resetNickReal(Player player) {
        UUID playerUUID = player.getUniqueId();
        repository.resetNickReal(playerUUID, player.getName());
        cache.removeNick(player);
    }

    // Verifica se o jogador tem um nick fake
    public boolean hasNickFake(Player player) {
        PlayerNick playerNick = getOrCreateNick(player);
        return playerNick.getNickFake() != null;
    }

    // Obtém o nick fake do jogador
    public String getNickFake(Player player) {
        PlayerNick playerNick = getOrCreateNick(player);
        return playerNick.getNickFake();
    }
}