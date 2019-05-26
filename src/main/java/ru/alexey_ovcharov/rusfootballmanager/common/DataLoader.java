package ru.alexey_ovcharov.rusfootballmanager.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Contract;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.LocalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.MasteryLevel;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.sponsor.Sponsor;
import ru.alexey_ovcharov.rusfootballmanager.entities.sponsor.Status;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Alexey
 */
public class DataLoader {

    private static final int DEFAULT_SUPPORT = 40;
    private static final int DEFAULT_TEAMWORK = 35;

    private DataLoader() {

    }

    @Nonnull
    public static List<League> loadLeagues(String configName) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        NodeList leaguesList;
        try {
            documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(configName));
            document.getDocumentElement().normalize();
            leaguesList = document.getElementsByTagName("league");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int leaguesCount = leaguesList.getLength();
        if (leaguesCount > 0) {
            List<League> leagues = new ArrayList<>(leaguesCount);
            for (int i = 0; i < leaguesCount; i++) {
                Node leagueNode = leaguesList.item(i);
                if (leagueNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element leagueElement = (Element) leagueNode;
                    String leagueName = leagueElement.getAttribute("name");
                    String leagueLevel = leagueElement.getAttribute("level");
                    int recommendedExperience = Integer.parseInt(leagueElement.getAttribute("experience"));
                    MasteryLevel masteryLevel = MasteryLevel.valueOf(leagueLevel);
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
        return Collections.emptyList();
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
                team.setSponsor(new Sponsor(sponsorName, Status.valueOf(sponsorStatus)));
                team.setSupport(DEFAULT_SUPPORT);
                team.setTeamwork(DEFAULT_TEAMWORK);
                league.addTeam(team);
                List<Player> players = createPlayers(masteryLevel);
                players.stream()
                       .peek(player -> player.setContract(new Contract(Randomization.getValueInBounds(1, 6),
                               MoneyHelper.calculatePayForMatch(player))))
                       .forEach(team::addPlayer);
                IntStream.range(0, 5)
                         .forEach(index -> team.addJuniorPlayer());
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
