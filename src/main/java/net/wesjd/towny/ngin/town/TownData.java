package net.wesjd.towny.ngin.town;

public class TownData {

    private String _town;
    private TownRank _townRank;

    public TownData(String _town, TownRank _townRank) {
        this._town = _town;
        this._townRank = _townRank;
    }

    public String getTown() {
        return _town;
    }

    public TownRank getTownRank() {
        return _townRank;
    }
}
