package br.com.pulse.pulsenick.config;

import com.tomkeuper.bedwars.api.language.Language;
import org.bukkit.configuration.file.YamlConfiguration;

import static br.com.pulse.pulsenick.config.ConfigPaths.*;

public class MessagesData {
    public MessagesData() {
        setup();
    }

    private void setup() {
        for (Language l : Language.getLanguages()) {
            YamlConfiguration yml = l.getYml();
            switch (l.getIso()) {
                case "pt":
                    yml.addDefault(NO_PERM, "&cVocê não tem permissão para isso!");
                    yml.addDefault(NICK_CHANGE, "&aSeu nick foi alterado para {NAME}!");
                    yml.addDefault(NICK_RESET, "&aSeu nick foi resetado");
                    break;
                default:
                    yml.addDefault(NO_PERM, "&cYou don't have permission for that!");
                    yml.addDefault(NICK_CHANGE, "&aHis nick was changed to {NAME}!");
                    yml.addDefault(NICK_RESET, "&aYour nick has been reset");
                    break;
            }
        }
    }
}
