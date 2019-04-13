package ru.alexey_ovcharov.rusfootballmanager.entities.transfer;

import java.util.*;
import java.util.stream.Collectors;

import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * Трансферный рынок
 *
 * @author Alexey
 */
public class Market {

    private static final Market market = new Market();
    private final List<Transfer> players = new ArrayList<>();
    private final Set<Offer> offers = new HashSet<>();
    private final Map<Team, List<Transfer>> cache = new HashMap<>();

    private Market() {
    }

    public static Market getInstance() {
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
                return;
            }
        }
    }

    public List<Transfer> getTransfers() {
        return players;
    }

    public List<Transfer> getTransfers(Filter filter) {
        if (filter != null) {
            return players.stream()
                          .filter(filter::accept)
                          .collect(Collectors.toList());
        }
        return players;
    }

    public List<Transfer> getTransfers(Status transferStatus) {
        return players.stream()
                      .filter(tr -> tr.getStatus() == transferStatus)
                      .collect(Collectors.toList());
    }

    public List<Transfer> getTransfers(Team team) {
        List<Transfer> playersFromTeam;
        if (cache.containsKey(team)) {
            playersFromTeam = cache.get(team);
        } else {
            playersFromTeam = players.stream()
                                     .filter(Objects::nonNull)
                                     .filter(player -> player.getTeam() == team)
                                     .collect(Collectors.toList());
            cache.put(team, playersFromTeam);
        }
        return playersFromTeam;
    }

    public List<Transfer> getTransfers(Team team, Filter filter) {
        List<Transfer> playersFromTeam = getTransfers(team);
        if (filter != null) {
            return playersFromTeam.stream()
                                  .filter(filter::accept)
                                  .collect(Collectors.toList());
        } else {
            return playersFromTeam;
        }
    }

    public List<Offer> getOffers(Team team) {
        return offers.stream()
                     .filter(offer -> offer.getFrom() == team)
                     .collect(Collectors.toList());
    }

    public void makeOffer(Offer offer) {
        this.offers.add(offer);
    }

    public void removeOffer(Offer selectedOffer) {
        this.offers.remove(selectedOffer);
    }

}
