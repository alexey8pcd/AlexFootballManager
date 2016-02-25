package rusfootballmanager.transfers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class TransferMarket {

    private static TransferMarket market;
    private List<TransferPlayer> players;
    private Map<Team, List<TransferPlayer>> cache;

    private TransferMarket() {
        players = new ArrayList<>();
        cache = new HashMap<>();
    }

    public static TransferMarket getInstance() {
        if (market == null) {
            market = new TransferMarket();
        }
        return market;
    }

    public void clear() {
        players.clear();
    }

    public void addPlayer(Player player, Team team, TransferStatus status) {
        players.add(new TransferPlayer(player, team, status));
    }

    public void removePlayer(Player player) {
        for (TransferPlayer transferPlayer : players) {
            if (transferPlayer.getPlayer() == player);
            players.remove(transferPlayer);
            return;
        }
    }

    public List<TransferPlayer> getTransfersByTeamWithoutFilter(Team team) {
        return getTransfersByTeam(TransferStatus.ANY, team, null);
    }

    public List<TransferPlayer> getTransfersByTeam(TransferStatus transferStatus, 
            Team team, Filter filter) {
        List<TransferPlayer> result;
        if (cache.containsKey(team)) {
            result = cache.get(team);
        } else {
            result = new ArrayList<>(team.getPlayersCount());
            for (TransferPlayer player : players) {
                if (player.getTeam() == team) {
                    result.add(player);
                }
            }
            cache.put(team, result);
        }
        chooseByStatus(transferStatus, result);
        if (filter != null) {
            filter.filter(result);
        }
        return result;
    }

    public List<TransferPlayer> getTransfersWithoutFilter() {
        return getTransfers(TransferStatus.ANY, null);
    }

    public List<TransferPlayer> getTransfers(TransferStatus transferStatus, Filter filter) {
        List<TransferPlayer> result = new ArrayList<>(players);
        chooseByStatus(transferStatus, result);
        if (filter != null) {
            filter.filter(result);
        }
        return result;
    }

    private void chooseByStatus(TransferStatus transferStatus, List<TransferPlayer> result) {
        switch (transferStatus) {
            case ON_CONTRACT:
                result.removeIf((p) -> {
                    return p.getStatus() != TransferStatus.ON_CONTRACT;
                });
                break;
            case ON_TRANSFER:
                result.removeIf((p) -> {
                    return p.getStatus() != TransferStatus.ON_TRANSFER;
                });
                break;
            case TO_RENT:
                result.removeIf((p) -> {
                    return p.getStatus() != TransferStatus.TO_RENT;
                });
                break;
            case ON_TRANSFER_OR_TO_RENT:
                result.removeIf((p) -> {
                    return p.getStatus() != TransferStatus.TO_RENT
                            && p.getStatus() != TransferStatus.TO_RENT;
                });
                break;
        }
    }

}
