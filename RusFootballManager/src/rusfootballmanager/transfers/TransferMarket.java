package rusfootballmanager.transfers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import rusfootballmanager.entities.Offer;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;

/**
 * @author Alexey
 */
public class TransferMarket {

    private static TransferMarket market;
    private List<TransferPlayer> players;
    private Set<Offer> offers;
    private Map<Team, Stream<TransferPlayer>> cache;

    private TransferMarket() {
        players = new ArrayList<>();
        cache = new HashMap<>();
        offers = new HashSet<>();
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
        Stream<TransferPlayer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = players.parallelStream().filter(
                    player -> player.getTeam() == team);
            cache.put(team, playersFromTeam);
        }
        Stream choosedByStatus = chooseByStatus(transferStatus, playersFromTeam);
        if (filter != null) {
            choosedByStatus = filter.filter(choosedByStatus);
        }
        List result = (List) choosedByStatus.collect(Collectors.toList());
        return result;
    }

    public List<TransferPlayer> getTransfersWithoutFilter() {
        return getTransfers(TransferStatus.ANY, null);
    }

    public List<TransferPlayer> getTransfers(TransferStatus transferStatus, Filter filter) {
        Stream choosedByStatus = chooseByStatus(transferStatus, players.parallelStream());
        if (filter != null) {
            choosedByStatus = filter.filter(choosedByStatus);
        }
        List result = (List) choosedByStatus.collect(Collectors.toList());
        return result;
    }

    public List<TransferPlayer> getTransfers(TransferStatus transferStatus) {
        return getTransfers(transferStatus, null);
    }

    public List<Offer> getDesiredPlayers(Team team) {
        List<Offer> myOffers = new ArrayList<>();
        offers.parallelStream().filter(off -> off.getFrom() == team)
                .forEach(off -> myOffers.add(off));
        return myOffers;
    }

    private Stream chooseByStatus(TransferStatus transferStatus, Stream<TransferPlayer> stream) {
        switch (transferStatus) {
            case ON_CONTRACT:
                return stream.filter(tr -> {
                    return tr.getStatus() != TransferStatus.ON_CONTRACT;
                });

            case ON_TRANSFER:
                return stream.filter(tr -> {
                    return tr.getStatus() != TransferStatus.ON_TRANSFER;
                });
            case TO_RENT:
                return stream.filter(tr -> {
                    return tr.getStatus() != TransferStatus.TO_RENT;
                });
            case ON_TRANSFER_OR_RENT:
                return stream.filter(tr -> {
                    return tr.getStatus() != TransferStatus.TO_RENT
                            && tr.getStatus() != TransferStatus.TO_RENT;
                });
        }
        return stream;
    }

    public void makeOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void removeOffer(Offer selectedOffer) {
        throw new UnsupportedOperationException();
    }

}
