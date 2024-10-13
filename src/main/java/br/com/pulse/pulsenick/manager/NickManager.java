package br.com.pulse.pulsenick.manager;

import br.com.pulse.pulsenick.storage.SQLite;
import org.bukkit.entity.Player;

public class NickManager {

    private final SQLite sqLite;

    // Construtor que inicializa a instância de SQLite
    // Constructor that initializes the SQLite instance
    public NickManager(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    /**
     * Aplica o nick fake ao jogador e salva no banco de dados
     * Applies the fake nickname to the player and saves it to the database
     *
     * @param player O jogador a ter o nick fake aplicado
     *               The player to apply the fake nickname to
     * @param nickFake O nick fake a ser aplicado
     *                 The fake nickname to apply
     */
    public void applyNickFake(Player player, String nickFake) {
        String playerUUID = player.getUniqueId().toString();
        sqLite.savePlayer(playerUUID, nickFake);
    }

    /**
     * Redefine o nick real do jogador no banco de dados
     * Resets the player's real nickname in the database
     *
     * @param player O jogador cujo nick real será restaurado
     *               The player whose real nickname will be restored
     * @param nickReal O nick real do jogador
     *                 The player's real nickname
     */
    public void applyNickReal(Player player, String nickReal) {
        String playerUUID = player.getUniqueId().toString();
        sqLite.resetPlayer(playerUUID, nickReal);
    }

    /**
     * Verifica se o jogador está presente no banco de dados
     * Checks if the player is present in the database
     *
     * @param player O jogador a ser verificado
     *               The player to be checked
     * @return True se o jogador estiver no banco de dados, False caso contrário
     *         True if the player is in the database, False otherwise
     */
    public boolean isPlayerInDatabase(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return sqLite.isPlayerInDatabase(playerUUID);
    }

    /**
     * Obtém o nick fake de um jogador a partir do banco de dados
     * Retrieves the player's fake nickname from the database
     *
     * @param player O jogador cujo nick fake será recuperado
     *               The player whose fake nickname will be retrieved
     * @return O nick fake do jogador ou null se não encontrado
     *         The player's fake nickname or null if not found
     */
    public String getNickFake(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return sqLite.getNickFake(playerUUID);
    }

    /**
     * Obtém o nick real de um jogador a partir do banco de dados
     * Retrieves the player's real nickname from the database
     *
     * @param player O jogador cujo nick real será recuperado
     *               The player whose real nickname will be retrieved
     * @return O nick real do jogador ou null se não encontrado
     *         The player's real nickname or null if not found
     */
    public String getNickReal(Player player) {
        String playerUUID = player.getUniqueId().toString();
        return sqLite.getNickReal(playerUUID);
    }
}