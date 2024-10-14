package br.com.pulse.pulsenick.database;

import br.com.pulse.pulsenick.model.PlayerNick;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NickRepository {

    private final Connection connection;

    /**
     * Inicializa o repositório com uma conexão de banco de dados.
     * Recebe uma instância ativa de conexão para realizar as operações de armazenamento.
     * <p>
     * Initializes the repository with a database connection.
     * Receives an active connection instance to perform storage operations.
     *
     * @param connection Conexão ativa com o banco de dados.
     *                   Active connection to the database.
     */
    public NickRepository(Connection connection) {
        this.connection = connection;
    }

    /**
     * Cria a tabela "players" no banco de dados se ela não existir.
     * Esta tabela armazena informações de UUID, nickFake, e nickReal.
     * - uuid: Identificador único do jogador (chave primária).
     * - nickFake: Apelido falso do jogador, pode ser null.
     * - nickReal: Nome real do jogador.
     * <p>
     * Creates the "players" table in the database if it doesn't exist.
     * This table stores UUID, nickFake, and nickReal information.
     * - uuid: Unique player identifier (primary key).
     * - nickFake: Player's fake nickname, can be null.
     * - nickReal: Player's real nickname.
     */
    public void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY," +
                "nickFake TEXT," +
                "nickReal TEXT);";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating players table", e);
        }
    }

    /**
     * Verifica se o jogador está registrado no banco de dados com base no UUID.
     * Retorna true se o jogador estiver presente, caso contrário retorna false.
     * <p>
     * Checks if the player is registered in the database based on their UUID.
     * Returns true if the player is present, otherwise returns false.
     *
     * @param playerUUID UUID do jogador a ser verificado.
     *                   The player's UUID to check.
     * @return true se o jogador estiver registrado, false caso contrário.
     *         true if the player is registered, false otherwise.
     */
    public boolean isPlayerInDatabase(UUID playerUUID) {
        String sql = "SELECT 1 FROM players WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if player is in database", e);
        }
    }

    /**
     * Salva o apelido falso (nickFake) de um jogador no banco de dados.
     * Se o jogador já existir, seus dados serão atualizados.
     * <p>
     * Saves a player's fake nickname (nickFake) to the database.
     * If the player already exists, their data will be updated.
     *
     * @param playerUUID UUID do jogador a ser salvo.
     *                   The UUID of the player to save.
     * @param nickFake O apelido falso a ser salvo.
     *                 The fake nickname to save.
     */
    public void savePlayer(UUID playerUUID, String nickFake) {
        String sql = "REPLACE INTO players (uuid, nickFake) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, nickFake);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving player to database", e);
        }
    }

    /**
     * Recupera o apelido falso (nickFake) de um jogador com base no UUID.
     * Retorna o nickFake ou null se não for encontrado.
     * <p>
     * Retrieves the player's fake nickname (nickFake) based on their UUID.
     * Returns the nickFake or null if not found.
     *
     * @param playerUUID O UUID do jogador cujo apelido será recuperado.
     *                   The UUID of the player whose nickname will be retrieved.
     * @return O apelido falso do jogador ou null se não encontrado.
     *         The player's fake nickname or null if not found.
     */
    public String getNickFake(UUID playerUUID) {
        String sql = "SELECT nickFake FROM players WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nickFake");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving fake nickname", e);
        }
        return null;
    }

    /**
     * Recupera as informações de nick de um jogador a partir do banco de dados, com base no UUID fornecido.
     * Retorna um objeto PlayerNick contendo o nome real e o apelido falso do jogador.
     * Caso o jogador não seja encontrado, retorna null.
     * <p>
     * Retrieves the player's nick information from the database based on the provided UUID.
     * Returns a PlayerNick object containing the real name and the player's fake nickname.
     * If the player is not found, returns null.
     *
     * @param playerUUID UUID do jogador a ser buscado.
     *                   The UUID of the player to retrieve.
     * @return Um objeto PlayerNick com o nome real e apelido falso, ou null se não encontrado.
     *         A PlayerNick object with real name and fake nickname, or null if not found.
     */
    public PlayerNick getPlayerNick(UUID playerUUID) {
        String sql = "SELECT nickReal, nickFake FROM players WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, playerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String realNick = rs.getString("nickReal");
                String fakeNick = rs.getString("nickFake");
                return new PlayerNick(realNick, fakeNick);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving player nick", e);
        }
        return null;
    }

    /**
     * Atualiza o apelido real (nickReal) de um jogador no banco de dados.
     * Define o nickReal do jogador com base no UUID.
     * <p>
     * Updates the player's real nickname (nickReal) in the database.
     * Sets the player's real nickname based on their UUID.
     *
     * @param playerUUID UUID do jogador a ser atualizado.
     *                   The UUID of the player to update.
     * @param nickReal O apelido real do jogador.
     *                 The player's real nickname.
     */
    public void resetNickReal(UUID playerUUID, String nickReal) {
        String sql = "UPDATE players SET nickReal = ? WHERE uuid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nickReal);
            stmt.setString(2, playerUUID.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error resetting player nickname", e);
        }
    }
}