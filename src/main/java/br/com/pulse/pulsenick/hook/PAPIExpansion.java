package br.com.pulse.pulsenick.hook;

import br.com.pulse.pulsenick.cache.NickCache;
import br.com.pulse.pulsenick.database.NickRepository;
import br.com.pulse.pulsenick.model.PlayerNick;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPIExpansion extends PlaceholderExpansion {

    private final NickCache cache;
    private final NickRepository repository;

    /**
     * Construtor para inicializar o cache e o repositório.
     * <p>
     * Constructor to initialize cache and repository.
     *
     * @param cache Cache de nicks.
     *              Nick cache.
     * @param repository Repositório para o banco de dados.
     *                   Database repository.
     */
    public PAPIExpansion(NickCache cache, NickRepository repository) {
        this.cache = cache;
        this.repository = repository;
    }

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

    /**
     * Processa a solicitação de placeholder com base no jogador e parâmetro.
     * <p>
     * Processes the placeholder request based on the player and parameter.
     *
     * @param player O jogador.
     *               The player.
     * @param params O parâmetro solicitado.
     *               The requested parameter.
     * @return O valor da placeholder ou null se inválido.
     *         The placeholder value or null if invalid.
     */
    @Override
    public @Nullable String onPlaceholderRequest(final Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // Recuperar as informações de nick do cache ou banco de dados
        // Retrieve nick information from cache or database
        PlayerNick playerNick = getPlayerNick(player);

        if (playerNick == null) {
            return "";
        }

        String request = params.toLowerCase();
        return switch (request) {
            case "name" -> playerNick.getFakeNick() != null ? playerNick.getFakeNick() : playerNick.getRealNick();
            case "realname" -> playerNick.getRealNick();
            case "is_nicked" -> String.valueOf(playerNick.getFakeNick() != null);
            default -> null;
        };
    }

    /**
     * Recupera as informações de nick do jogador, utilizando o cache primeiro.
     * Caso não esteja no cache, busca no banco de dados e atualiza o cache.
     * <p>
     * Retrieves player nick information using cache first.
     * If not in cache, it fetches from the database and updates the cache.
     *
     * @param player O jogador.
     *               The player.
     * @return As informações de PlayerNick ou null se não encontrado.
     *         PlayerNick information or null if not found.
     */
    private PlayerNick getPlayerNick(Player player) {
        if (cache.hasNick(player)) {
            return cache.getNick(player);
        }

        // Se não estiver no cache, busca do banco de dados e adiciona ao cache
        // If not in cache, fetches from the database and adds to cache
        PlayerNick playerNick = repository.getPlayerNick(player.getUniqueId());
        if (playerNick != null) {
            cache.setNick(player, playerNick);
        }

        return playerNick;
    }
}