package ru.alexey_ovcharov.rusfootballmanager.career;

import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.alexey_ovcharov.rusfootballmanager.common.XMLParseable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Status;

/**
 * @author Alexey
 */
public class CareerSettings implements XMLParseable {

    private List<League> leagues;
    private List<Tournament> tournaments;
    private Market transferMarket;

    @Override
    public Element toXmlElement(Document document) {
        throw new UnsupportedOperationException("Еще не реализовано");
    }

    @Override
    public String toXmlString(Document document) {
        throw new UnsupportedOperationException("Еще не реализовано");
    }

    public boolean isNewCareer() {
        return leagues == null || transferMarket == null;
    }

    public List<League> getLeagues() throws Exception {
        if (leagues == null) {
            leagues = DataLoader.loadLeagues("config.xml");
        }
        if (transferMarket == null) {
            transferMarket = Market.getInstance();
            leagues.stream().map((league) -> league.getTeams()).
                    parallel().forEach((teams) -> {
                        teams.stream().forEach((team) -> {
                            List<Player> players = team.getAllPlayers();
                            players.stream().forEach((player) -> {
                                transferMarket.addPlayer(player,
                                        team, Status.ON_CONTRACT);
                            });
                        });
                    });
        }
        return leagues;
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void createTournaments(Date date) {
        tournaments = new ArrayList<>();
        if (leagues != null) {
            leagues.stream().forEach((league) -> {
                tournaments.add(new Tournament(date, league));
            });
        }
    }

    public void updateTournaments(Date date, Tournament exclude) {
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament tournament : tournaments) {
                if (tournament != exclude) {
                    tournament.updateToDate(date);
                }
            }
        }
    }

    public void simulateTransfers() {

    }

}