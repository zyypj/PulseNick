package br.com.pulse.pulsenick.command;

import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.util.Utility;
import dev.iiahmed.disguise.Disguise;
import dev.iiahmed.disguise.DisguiseManager;
import dev.iiahmed.disguise.SkinAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static br.com.pulse.pulsenick.config.ConfigPaths.*;

public class NickCommand implements CommandExecutor {

    private final NickManager nickManager;
    private final Utility utility;

    // Construtor que inicializa o gerenciador de nick e utilitário
    public NickCommand(NickManager nickManager, Utility utility) {
        this.nickManager = nickManager;
        this.utility = utility;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cEste comando só pode ser usado por jogadores.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§cUse: /nick <nome/reset>");
            return false;
        }

        String newNick = args[0];

        // Se o comando for para resetar o nick
        if (newNick.equalsIgnoreCase("reset")) {
            resetNick(player);
            return true;
        }

        // Se for para definir um novo nick
        setNewNick(player, newNick);
        return true;
    }

    /**
     * Aplica um novo nick ao jogador usando ModernDisguise e atualiza o banco de dados e cache.
     * @param player O jogador
     * @param newNick O novo nick a ser aplicado
     */
    private void setNewNick(Player player, String newNick) {
        Disguise disguise = Disguise.builder()
                .setName(newNick)
                .setSkin(SkinAPI.MOJANG, player.getUniqueId())
                .build();

        DisguiseManager.getProvider().disguise(player, disguise);
        nickManager.applyNickFake(player, newNick);

        player.sendMessage(utility.getMsg(player, NICK_CHANGE).replace("{NAME}", newNick));
    }

    /**
     * Reseta o nick do jogador para o nome real, removendo o nick fake.
     * Atualiza tanto no ModernDisguise quanto no banco de dados e cache.
     * @param player O jogador cujo apelido deve ser resetado
     */
    private void resetNick(Player player) {
        DisguiseManager.getProvider().undisguise(player);
        nickManager.resetNickReal(player);

        player.sendMessage(utility.getMsg(player, NICK_RESET));
    }
}