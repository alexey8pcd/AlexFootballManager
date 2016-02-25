package rusfootballmanager.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.common.XMLFormatter;
import rusfootballmanager.common.XMLParseable;

/**
 * @author Alexey
 */
public class League implements XMLParseable {

    private final String name;
    private final List<Team> teams;
    private int recommendedExperience;

    public int getRecommendedExperience() {
        return recommendedExperience;
    }

    public void setRecommendedExperience(int recommendedExperience) {
        this.recommendedExperience = recommendedExperience;
    }

    public League(String name, int teamsCount) {
        this.name = name;
        this.teams = new ArrayList<>(teamsCount);
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("-----");
        stringBuilder.append(name).append("-----\n");
        for (Team team : teams) {
            stringBuilder.append(team.toString());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public Element toXmlElement(Document document) {
        Element leagueElement = document.createElement("league");
        leagueElement.setAttribute("name", name);
        for (Team team : teams) {
            leagueElement.appendChild(team.toXmlElement(document));
        }
        return leagueElement;
    }

    @Override
    public String toXmlString(Document document) {
        try {
            return XMLFormatter.elementToString(toXmlElement(document));
        } catch (TransformerException ex) {
            System.err.println(ex);
            return "";
        }
    }

}
