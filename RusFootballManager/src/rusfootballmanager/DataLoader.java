package rusfootballmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import rusfootballmanager.common.CostCalculator;
import rusfootballmanager.common.Randomization;
import rusfootballmanager.entities.Contract;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.LocalPosition;
import rusfootballmanager.entities.Player;
import rusfootballmanager.entities.Sponsor;
import rusfootballmanager.entities.SponsorStatus;
import rusfootballmanager.entities.Team;
import rusfootballmanager.school.MasteryLevel;
import rusfootballmanager.school.PlayerCreator;

/**
 * @author Alexey
 */
public class DataLoader {

    public static List<League> loadLeagues(String configName) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(configName));
        document.getDocumentElement().normalize();
        NodeList leaguesList = document.getElementsByTagName("league");
        int leaguesCount = leaguesList.getLength();
        if (leaguesCount > 0) {
            List<League> leagues = new ArrayList<>(leaguesCount);
            for (int i = 0; i < leaguesCount; i++) {
                Node leagueNode = leaguesList.item(i);
                if (leagueNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element leagueElement = (Element) leagueNode;
                    String leagueName = leagueElement.getAttribute("name");
                    String leagueLevel = leagueElement.getAttribute("level");
                    int recommendedExperience
                            = Integer.parseInt(leagueElement.getAttribute("experience"));
                    MasteryLevel masteryLevel
                            = MasteryLevel.valueOf(leagueLevel);
                    NodeList teamsList = leagueElement.getElementsByTagName("team");
                    int teamsCount = teamsList.getLength();
                    if (teamsCount > 0) {
                        League league = createLeague(leagueName, teamsCount,
                                recommendedExperience, teamsList, masteryLevel);
                        leagues.add(league);
                    }
                }
            }
            return leagues;
        }
        return Collections.EMPTY_LIST;
    }

    private static League createLeague(String leagueName, int teamsCount,
            int recommendedExperience, NodeList teamsList,
            MasteryLevel masteryLevel) throws NumberFormatException {
        League league = new League(leagueName, teamsCount);
        league.setRecommendedExperience(recommendedExperience);
        for (int j = 0; j < teamsCount; j++) {
            Node teamNode = teamsList.item(j);
            if (teamNode.getNodeType() == Node.ELEMENT_NODE) {
                Element teamElement = (Element) teamsList.item(j);
                String teamName = teamElement.getAttribute("name");
                String city = teamElement.getAttribute("city");
                long budget = Long.parseLong(teamElement.getAttribute("budget"));
                String sponsorName = teamElement.getAttribute("sponsor");
                String sponsorStatus = teamElement.getAttribute("sponsor-level");
                Team team = new Team(teamName + " (" + city + ")", budget);
                team.setSponsor(new Sponsor(sponsorName,
                        SponsorStatus.valueOf(sponsorStatus)));
                league.addTeam(team);
                List<Player> players = createPlayers(masteryLevel);
                players.stream().map(player -> {
                    player.setContract(new Contract(Randomization.getValueInBounds(1, 6),
                            CostCalculator.calculatePayForMatch(
                                    player.getAge(), player.getAverage())));
                    return player;
                }).forEach(player -> {
                    team.addPlayer(player);
                });
                for (int i = 0; i < 5; i++) {
                    team.addJuniorPlayer();
                }
            }
        }
        return league;
    }

    private static List<Player> createPlayers(MasteryLevel masteryLevel) {
        List<Player> players = new ArrayList<>();
        LocalPosition[] values = LocalPosition.values();
        for (int i = 0; i < values.length * 2; i++) {
            players.add(PlayerCreator.createPlayer(values[i / 2], masteryLevel));
        }
        players.add(PlayerCreator.createPlayer(GlobalPosition.GOALKEEPER, masteryLevel));
        players.add(PlayerCreator.createPlayer(LocalPosition.CENTRAL_DEFENDER, masteryLevel));
        players.add(PlayerCreator.createPlayer(LocalPosition.CENTRAL_DEFENDER, masteryLevel));
        return players;
    }

}
