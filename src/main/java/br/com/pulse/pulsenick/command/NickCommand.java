package br.com.pulse.pulsenick.command;

import br.com.pulse.pulsenick.manager.NickManager;
import br.com.pulse.pulsenick.util.Utility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import dev.iiahmed.disguise.Disguise;
import dev.iiahmed.disguise.DisguiseManager;
import dev.iiahmed.disguise.SkinAPI;

import static br.com.pulse.pulsenick.config.ConfigPaths.*;

public class NickCommand implements CommandExecutor {

    private final NickManager nickManager;

    public NickCommand(NickManager nickManager) {
        this.nickManager = nickManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;

        if (args.length != 1) {
            player.sendMessage("§cUse: /nick <nome/reset>");
            return false;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            DisguiseManager.getProvider().undisguise(player);
            nickManager.applyNickReal(player, player.getName());
            player.sendMessage(Utility.getMsg(player, NICK_RESET));
            return true;
        }

        String newNick = args[0];

        // Aplicando o novo nick com ModernDisguise, incluindo a skin padrão
        Disguise disguise = Disguise.builder()
                .setName(newNick)
                .setSkin(SkinAPI.MOJANG, player.getUniqueId()) // Define a skin padrão com base no UUID do jogador
                .build();

        DisguiseManager.getProvider().disguise(player, disguise);
        nickManager.applyNickFake(player, newNick);

        player.sendMessage(Utility.getMsg(player, NICK_CHANGE).replace("{NAME}", newNick));
        return true;
    }
}