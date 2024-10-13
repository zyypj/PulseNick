package br.com.pulse.pulsenick.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NickRepository {

    private final Connection connection;

    // Construtor que inicializa a conexão com o banco de dados
    public NickRepository(Connection connection) {
        this.connection = connection;
    }

    // Método para criar a tabela de jogadores, se ela não existir
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

    // Verifica se o jogador está presente na base de dados
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

    // Salva o nickFake de um jogador no banco de dados
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

    // Retorna o nickFake do banco de dados com base no UUID do jogador
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

    // Atualiza o nick real do jogador no banco de dados
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