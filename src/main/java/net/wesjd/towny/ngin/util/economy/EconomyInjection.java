package net.wesjd.towny.ngin.util.economy;

import com.google.inject.Inject;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.wesjd.towny.ngin.player.OfflineTownyPlayer;
import net.wesjd.towny.ngin.player.PlayerManager;

import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

/**
 * Simple injection of our custom economy in vault
 */
public class EconomyInjection extends AbstractEconomy {

    /**
     * The injected player manager
     */
    @Inject
    private PlayerManager _playerManager;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "towny-ngin";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return NumberFormat.getCurrencyInstance().format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "dollars";
    }

    @Override
    public String currencyNameSingular() {
        return "dollar";
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if(amount < 0) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't deposit negative");

        final UUID playerUuid = _playerManager.getUUIDFor(playerName);
        OfflineTownyPlayer player = _playerManager.getPlayer(playerUuid);
        boolean createdOffline = false;
        if(player == null) {
            player = _playerManager.createOfflineTownyPlayer(playerUuid);
            createdOffline = true;
        }
        player.addMoney(amount);
        if(createdOffline) player.save();
        return new EconomyResponse(amount, player.getMoney(), EconomyResponse.ResponseType.SUCCESS, "none");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }
    
    //------------------------- DOESN'T NEED IMPLEMENTED ------------------------

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        return 0;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

}
