package net.wesjd.towny.ngin.command.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import li.l1t.common.intake.i18n.Translator;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

public class TownyPlayerProvider extends BaseProvider<TownyPlayer> {

    private final Translator _translator;
    private final PlayerManager _playerManager;

    public TownyPlayerProvider(Translator translator, PlayerManager playerManager) {
        _translator = translator;
        _playerManager = playerManager;
    }

    @Nullable
    @Override
    public TownyPlayer get(CommandArgs arguments, List<? extends Annotation> list) throws ArgumentException, ProvisionException {
        final CommandSender sender = arguments.getNamespace().get(CommandSender.class);
        if(!(sender instanceof Player)) throw new CommandExitMessage(_translator.translate("PlayerOnlyCommand"));
        return _playerManager.getPlayer((Player) sender);
    }

}
