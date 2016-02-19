
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import rusfootballmanager.common.Constants;
import rusfootballmanager.entities.GlobalPosition;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Team;
import rusfootballmanager.school.PlayerCreator;
import rusfootballmanager.simulation.TournamentPair;
import rusfootballmanager.simulation.TournamentSchedule;

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

//    @Test
    public void testConvertToXml() throws ParserConfigurationException, IOException, TransformerException {
        League league = createLeague();
        Document document = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().newDocument();
        String result = league.toXmlString(document);

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(new File("test.xml")))) {
            bufferedWriter.write(result);
            bufferedWriter.flush();
        }
    }

//    @Test
    public void testScheduleWithHolidays() throws ParseException {
        League league = createLeague();
        Date start = DATE_FORMAT.parse("01.08.2016");
        Date end = DATE_FORMAT.parse("31.06.2017");
        Date holidaysStart = DATE_FORMAT.parse("15.11.2016");
        Date holidaysEnd = DATE_FORMAT.parse("01.03.2017");
        TournamentSchedule schedule = new TournamentSchedule(start, end,
                holidaysStart, holidaysEnd, 2, league.getTeams());
        Map<Date, List<TournamentPair>> schedule1 = schedule.getSchedule();
        for (Map.Entry<Date, List<TournamentPair>> entry : schedule1.entrySet()) {
            Date date = entry.getKey();
            List<TournamentPair> pairs = entry.getValue();
            System.out.println("Date: " + date);
            System.out.println("Matches: " + Arrays.toString(pairs.toArray()));
        }
    }

//    @Test
    public void testAvg() {
        for (int j = 0; j < 10; j++) {
            System.out.println("---" + j);
            for (int i = 0; i < 50; i++) {
                System.out.println(PlayerCreator.randomNormalLeftAverage(j));
            }
        }
    }
    
    @Test
    public void testPlayer(){
        for (int i = 0; i < 59; i++) {
            System.out.println(PlayerCreator.createPlayer(GlobalPosition.FORWARD));
        }
    }
    
    
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private League createLeague() {
        int teamsCount = 16;
        League league = new League("league1", teamsCount);
        for (int i = 0; i < teamsCount; i++) {
            Team team = new Team("team" + (i + 1));
            league.addTeam(team);
            for (int j = 0; j < Team.MAX_PLAYERS_COUNT; j++) {
                team.addPlayer(PlayerCreator.createYoungPlayerSimple());
            }
        }
        return league;
    }

}
