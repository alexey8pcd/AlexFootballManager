package ru.alexey_ovcharov.rusfootballmanager.career;

import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.TransferStatus;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexey
 */
public class CareerSettings {

    private List<League> leagues;
    private List<Tournament> tournaments;
    private Market transferMarket;

    public boolean isNewCareer() {
        return leagues == null || transferMarket == null;
    }

    @Nonnull
    public List<League> getLeagues() throws Exception {
        if (leagues == null) {
            leagues = DataLoader.loadLeagues("config.xml");
        }
        return leagues;
    }

    public void createTransfers(Team myTeam, LocalDate date) {
        if (transferMarket == null) {
            transferMarket = Market.getInstance();
            leagues.stream()
                   .map(League::getTeams)
                   .flatMap(List::stream)
                   .forEach(team -> {
                       if (myTeam != team) {
                           team.addJuniors();
                       }
                       List<Player> players = team.getAllPlayers();
                       players.forEach(player -> transferMarket.addPlayer(player, team, TransferStatus.ON_CONTRACT));
                   });
            simulateTransfers(date, myTeam);
        }
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void createTournaments(LocalDate date) {
        tournaments = new ArrayList<>();
        if (leagues != null) {
            leagues.forEach(league -> tournaments.add(new Tournament(date, league)));
        }
    }

    public void updateTournaments(LocalDate date, Tournament exclude) {
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament tournament : tournaments) {
                if (tournament != exclude) {
                    tournament.nextEvent();
                }
            }
        }
    }

    public void simulateTransfers(LocalDate date, Team myTeam) {
        transferMarket.processOffers(date);
        leagues.stream()
               .map(League::getTeams)
               .flatMap(Collection::stream)
               .filter(team -> team != myTeam)
               .forEach(team -> team.simulateTransfers(date));
    }

}
