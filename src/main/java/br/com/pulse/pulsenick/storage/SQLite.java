package br.com.pulse.pulsenick.storage;

import com.tomkeuper.bedwars.api.BedWars;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class SQLite {

    private final String databasePath;
    private Connection connection;

    public SQLite() {
        BedWars bw2023Api = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        this.databasePath = "jdbc:sqlite:" + bw2023Api.getAddonsPath().getPath() + File.separator + "PulseNick";
    }

    // Método para conectar ao banco de dados
    // Method to connect to the database
    public void connect() {
        try {
            connection = DriverManager.getConnection(databasePath);
        } catch (SQLException e) {
            logError("Erro ao conectar ao banco de dados", e);
        }
    }

    // Fecha a conexão quando não for mais necessária
    // Closes the connection when no longer needed
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logError("Erro ao fechar a conexão com o banco de dados", e);
            }
        }
    }

    /**
     * Cria a tabela de jogadores se não existir
     * Creates the players table if it does not exist
     */
    public void createDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS players(" +
                "uuid TEXT PRIMARY KEY," +
                "nickFake TEXT," +
                "nickReal TEXT" +
                ");";

        executeUpdate(sql);
    }

    /**
     * Salva o nick fake de um jogador no banco de dados
     * Saves the player's fake nick in the database
     *
     * @param playerUUID O UUID do jogador
     *                  The player's UUID
     * @param nickFake O nick fake do jogador
     *                 The player's fake nick
     */
    public void savePlayer(String playerUUID, String nickFake) {
        String sql = "INSERT OR REPLACE INTO players (uuid, nickFake) VALUES (?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID);
            pstmt.setString(2, nickFake);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError("Error saving player " + playerUUID, e);
        }
    }

    /**
     * Redefine o nick real de um jogador no banco de dados
     * Resets the player's real nick in the database
     *
     * @param playerUUID O UUID do jogador
     *                  The player's UUID
     * @param nickReal O nick real do jogador
     *                 The player's real nick
     */
    public void resetPlayer(String playerUUID, String nickReal) {
        String sql = "UPDATE players SET nickReal = ? WHERE uuid = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nickReal);
            pstmt.setString(2, playerUUID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logError("Error resetting player " + playerUUID, e);
        }
    }

    /**
     * Obtém o nick fake de um jogador a partir do banco de dados
     * Retrieves the player's fake nick from the database
     *
     * @param playerUUID O UUID do jogador
     *                  The player's UUID
     * @return O nick fake do jogador ou null se não encontrado
     *         The player's fake nick or null if not found
     */
    public String getNickFake(String playerUUID) {
        String sql = "SELECT nickFake FROM players WHERE uuid = ?;";
        return executeQuery(sql, playerUUID, "nickFake");
    }

    /**
     * Obtém o nick real de um jogador a partir do banco de dados
     * Retrieves the player's real nick from the database
     *
     * @param playerUUID O UUID do jogador
     *                  The player's UUID
     * @return O nick real do jogador ou null se não encontrado
     *         The player's real nick or null if not found
     */
    public String getNickReal(String playerUUID) {
        String sql = "SELECT nickReal FROM players WHERE uuid = ?;";
        return executeQuery(sql, playerUUID, "nickReal");
    }

    /**
     * Método para executar uma atualização SQL
     * Method to execute a SQL update
     *
     * @param sql A instrução SQL a ser executada
     *            The SQL statement to execute
     */
    private void executeUpdate(String sql) {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Error executing update: " + e.getMessage());
        }
    }

    /**
     * Método para executar uma consulta SQL
     * Method to execute a SQL query
     *
     * @param sql A instrução SQL a ser executada
     *            The SQL statement to execute
     * @param playerUUID O UUID do jogador
     *                  The player's UUID
     * @param columnName O nome da coluna a ser retornada
     *                    The name of the column to return
     * @return O valor da coluna ou null se não encontrado
     *         The column value or null if not found
     */
    private String executeQuery(String sql, String playerUUID, String columnName) {
        String result = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, playerUUID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString(columnName);
            }
        } catch (SQLException e) {
            logError("Error retrieving " + columnName + " for player " + playerUUID, e);
        }

        return result;
    }

    /**
     * Método para registrar erros no console
     * Method to log errors to the console
     *
     * @param message A mensagem do erro
     *                The error message
     * @param e A exceção que ocorreu
     *          The exception that occurred
     */
    private void logError(String message, Exception e) {
        Bukkit.getLogger().severe(message + ": " + e.getMessage());
    }
}