package rusfootballmanager.entities.tournament;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */

public class League {

    @XmlAttribute
    private String name;
    @XmlAttribute
    private int recommendedExperience;
    @XmlElement
    private List<Team> teams;

    public int getRecommendedExperience() {
        return recommendedExperience;
    }

    public void setRecommendedExperience(int recommendedExperience) {
        this.recommendedExperience = recommendedExperience;
    }

    public League() {
        teams = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
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

}
