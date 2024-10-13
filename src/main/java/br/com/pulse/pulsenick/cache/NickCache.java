package br.com.pulse.pulsenick.cache;

import br.com.pulse.pulsenick.model.PlayerNick;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NickCache {

    // Armazena o nick real e fake dos jogadores em memória
    // Stores the real and fake nicknames of players in memory
    private final Map<UUID, PlayerNick> nickCache = new HashMap<>();

    // Retorna as informações de nick de um jogador a partir do cache
    // Retrieves the player's nick information from the cache
    public PlayerNick getNick(Player player) {
        return nickCache.get(player.getUniqueId());
    }

    // Salva ou atualiza as informações de nick de um jogador no cache
    // Saves or updates the player's nick information in the cache
    public void setNick(Player player, PlayerNick playerNick) {
        nickCache.put(player.getUniqueId(), playerNick);
    }

    // Remove as informações de nick de um jogador do cache
    // Removes the player's nick information from the cache
    public void removeNick(Player player) {
        nickCache.remove(player.getUniqueId());
    }

    // Verifica se o jogador está presente no cache
    // Checks if the player is present in the cache
    public boolean hasNick(Player player) {
        return nickCache.containsKey(player.getUniqueId());
    }

    // Limpa o cache de todos os jogadores
    // Clears the cache of all players
    public void clear() {
        nickCache.clear();
    }
}