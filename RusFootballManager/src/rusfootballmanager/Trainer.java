package rusfootballmanager;

import rusfootballmanager.entities.Team;
import rusfootballmanager.simulation.League;

/**
 * @author Alexey
 */
public class Trainer {

    private String login;
    private Team team;
    private League league;
    private CareerSettings settings;
    private int experience;

    public Trainer(String login) {
        this.login = login;
        settings = new CareerSettings();
    }

}
