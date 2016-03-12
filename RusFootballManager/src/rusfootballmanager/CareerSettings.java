package rusfootballmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import rusfootballmanager.common.XMLParseable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Team;
import rusfootballmanager.transfers.TransferMarket;
import rusfootballmanager.transfers.TransferStatus;

/**
 * @author Alexey
 */
public class CareerSettings implements XMLParseable {

    private List<League> leagues;
    private List<Tournament> tournaments;
    private TransferMarket transferMarket;

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
            transferMarket = TransferMarket.getInstance();
            leagues.stream().map((league) -> league.getTeams()).
                    parallel().forEach((teams) -> {
                        teams.stream().forEach((team) -> {
                            List<Player> players = team.getAllPlayers();
                            players.stream().forEach((player) -> {
                                transferMarket.addPlayer(player, 
                                        team, TransferStatus.ON_CONTRACT);
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
        for (League league : leagues) {
            tournaments.add(new Tournament(date, league));
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
