package rusfootballmanager.entities.transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import rusfootballmanager.entities.player.Player;
import rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class Market {

    private static Market market;
    private List<Transfer> players;
    private Set<Offer> offers;
    private Map<Team, List<Transfer>> cache;

    private Market() {
        players = new ArrayList<>();
        cache = new HashMap<>();
        offers = new HashSet<>();
    }

    public static Market getInstance() {
        if (market == null) {
            market = new Market();
        }
        return market;
    }

    public void clear() {
        players.clear();
    }

    public void addPlayer(Player player, Team team, Status status) {
        players.add(new Transfer(player, team, status));
    }

    public void removePlayer(Player player) {
        for (Transfer transferPlayer : players) {
            if (transferPlayer.getPlayer() == player) {
                players.remove(transferPlayer);
            }
            return;
        }
    }

    public List<Transfer> getTransfers() {
        return players;
    }

    public List<Transfer> getTransfers(Filter filter) {
        if (filter != null) {
            return players.parallelStream()
                    .filter(tr -> filter.accept(tr))
                    .collect(Collectors.toList());
        }
        return players;
    }

    public List<Transfer> getTransfers(Status transferStatus) {
        return players.parallelStream()
                .filter(tr -> tr.getStatus() == transferStatus)
                .collect(Collectors.toList());
    }

    public List<Transfer> getTransfers(Team team) {
        List<Transfer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = players.stream().filter(
                    player -> player != null && player.getTeam() == team).
                    collect(Collectors.toList());
            cache.put(team, playersFromTeam);
        }
        return playersFromTeam;
    }

    public List<Transfer> getTransfers(Team team, Filter filter) {
        List<Transfer> playersFromTeam = getTransfers(team);
        if (filter != null) {
            return playersFromTeam.stream().
                    filter(tr -> filter.accept(tr)).
                    collect(Collectors.toList());
        } else {
            return playersFromTeam;
        }
    }

    public List<Offer> getOffers(Team team) {
        List<Offer> offersByTeam = offers.parallelStream().
                filter(off -> off.getFrom() == team)
                .collect(Collectors.toList());
        return offersByTeam;
    }

    public void makeOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void removeOffer(Offer selectedOffer) {
        this.offers.remove(selectedOffer);
    }

}
