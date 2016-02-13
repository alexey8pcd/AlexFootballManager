/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Team;
import rusfootballmanager.school.YoungPlayerCreator;

/**
 *
 * @author Алексей
 */
public class Tests {

    public Tests() {
    }

    @Before
    public void setUp() {
        Logger.getLogger("log").setLevel(Level.FINE);
    }

    public void testConvertToXml() throws JAXBException {
        int teamsCount = 12;
        League league = new League("league1", teamsCount);
        for (int i = 0; i < teamsCount; i++) {
            Team team = new Team("team" + (i + 1));
            league.addTeam(team);
            for (int j = 0; j < Team.MAX_PLAYERS_COUNT; j++) {
                team.addPlayer(YoungPlayerCreator.createYoungPlayer());
            }
        }

        JAXBContext context = JAXBContext.newInstance(League.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(league, System.out);
    }

}
