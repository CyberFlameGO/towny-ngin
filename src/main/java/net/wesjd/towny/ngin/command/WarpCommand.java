package net.wesjd.towny.ngin.command;

public class WarpCommand {

    /*@Command(aliases = "to", desc = "Warps to a warp", min = 1)
    public void warpTo(@Sender TownyPlayer player, @Text @Optional String warpName) {
        if (player.getTown() == null) player.message(ChatColor.RED + "You aren't apart of any town");
        else if (warpName == null) {
            final Town town = player.getTown();
            final Set<String> warps = town.getWarps().keySet();

            if (warps.isEmpty()) player.message(YELLOW + "Warps: " + RED + "None");
            else warps.forEach(warp ->
                    new FancyMessage(" - ").color(GOLD)
                            .then(warp).color(RED).tooltip("Click to warp to " + warp).command("/warp " + warp)
                            .send(player.getWrapped()));
        } else {
            final Location warp = player.getTown().getWarps().get(warpName.toLowerCase());

            if (warp == null) player.message(ChatColor.RED + "Warp " + warpName + " doesn't exist");
            else player.getWrapped().teleport(warp);
        }
    }*/

}
