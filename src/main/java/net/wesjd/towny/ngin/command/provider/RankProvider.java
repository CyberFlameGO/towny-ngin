package net.wesjd.towny.ngin.command.provider;

import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.ProvisionException;
import li.l1t.common.intake.exception.CommandExitMessage;
import net.wesjd.towny.ngin.player.Rank;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class RankProvider extends BaseProvider<Rank> {

    @Nullable
    @Override
    public Rank get(CommandArgs arguments, List<? extends Annotation> list) throws ArgumentException, ProvisionException {
        try {
            return Rank.valueOf(arguments.next().toUpperCase());
        } catch (Exception ex) {
            throw new CommandExitMessage("Invalid rank type. Valid values are: " + Arrays.toString(Rank.values()));
        }
    }

}
