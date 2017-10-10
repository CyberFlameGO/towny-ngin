package net.wesjd.towny.ngin.command.framework.argument.provider;

import net.wesjd.towny.ngin.command.framework.argument.Arguments;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.lang.reflect.Parameter;
import java.util.UUID;

/**
 * Provider for {@link OfflineTownyPlayer}s
 */
public class OfflineTownyPlayerProvider implements ArgumentProvider<OfflineTownyPlayer> {

    @Inject
    private PlayerManager playerManager;

    @Override
    public OfflineTownyPlayer get(Parameter parameter, Arguments arguments) {
        final String input = arguments.next();

        UUID uuid;
        try {
            uuid = UUID.fromString(input);
        } catch (IllegalArgumentException e) {
            try {
                uuid = playerManager.getUUIDFor(input);
            } catch (RuntimeException ex) {
                return null;
            }
        }

        final Player online = Bukkit.getPlayer(uuid);
        if(online != null) return playerManager.getPlayer(online);
        else return playerManager.createOfflineTownyPlayer(uuid);
    }

}
