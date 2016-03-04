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
    private Map<Team, List<TransferPlayer>> cache;

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

    public List<TransferPlayer> getTransfers() {
        return players;
    }

    public List<TransferPlayer> getTransfers(Filter filter) {
        if (filter != null) {
            return players.parallelStream()
                    .filter(tr -> filter.accept(tr))
                    .collect(Collectors.toList());
        }
        return players;
    }

    public List<TransferPlayer> getTransfers(TransferStatus transferStatus) {
        return players.parallelStream()
                .filter(tr -> tr.getStatus() == transferStatus)
                .collect(Collectors.toList());
    }

    public List<TransferPlayer> getTransfers(Team team) {
        List<TransferPlayer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = players.parallelStream().filter(
                    player -> player.getTeam() == team).collect(Collectors.toList());
            cache.put(team, playersFromTeam);
        }
        return playersFromTeam;
    }

    public List<TransferPlayer> getTransfers(Team team, Filter filter) {
        List<TransferPlayer> playersFromTeam = getTransfers(team);
        if (filter != null) {
            return playersFromTeam.parallelStream().
                    filter(tr -> filter.accept(tr)).
                    collect(Collectors.toList());
        } else {
            return playersFromTeam;
        }
    }

    public List<Offer> getOffers(Team team) {
        List<Offer> offersByTeam = new ArrayList<>();
        offersByTeam.parallelStream().filter(off -> off.getFrom() == team)
                .forEach(off -> offersByTeam.add(off));
        return offersByTeam;
    }

    public void makeOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void removeOffer(Offer selectedOffer) {
        throw new UnsupportedOperationException();
    }

}
