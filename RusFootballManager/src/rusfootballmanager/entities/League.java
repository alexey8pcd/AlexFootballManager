package rusfootballmanager.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Alexey
 */
@XmlRootElement(name = "league")
@XmlAccessorType(XmlAccessType.FIELD)
public class League {

    public League() {
    }

    private String name;

    @XmlElement(name = "teams")
    private List<Team> teams;

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

}
