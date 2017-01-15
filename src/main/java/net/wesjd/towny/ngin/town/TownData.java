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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TownData{");
        sb.append("_town='").append(_town).append('\'');
        sb.append(", _townRank=").append(_townRank);
        sb.append('}');
        return sb.toString();
    }
}
