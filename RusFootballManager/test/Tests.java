
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.MasteryLevel;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Opponents;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Schedule;

/**
 *
 * @author Алексей
 */
public class Tests {

    public Tests() {
    }

    @Before
    public void setUp() {
    }   

//    @Test
    public void testScheduleWithHolidays() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        League league = createLeague();
        Date start = dateFormat.parse("01.08.2016");
        Date end = dateFormat.parse("31.06.2017");
        Date holidaysStart = dateFormat.parse("15.11.2016");
        Date holidaysEnd = dateFormat.parse("01.03.2017");
        Schedule tournamentSchedule = new Schedule(start, end,
                holidaysStart, holidaysEnd, 2, league.getTeams());
        Map<Date, List<Opponents>> schedule = tournamentSchedule.getSchedule();
        schedule.entrySet().stream().forEach((entry) -> {
            Date date = entry.getKey();
            List<Opponents> pairs = entry.getValue();
            System.out.println("Date: " + date);
            System.out.println("Matches: " + Arrays.toString(pairs.toArray()));
        });
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

//    @Test
    public void testPlayer() {
        for (MasteryLevel level : MasteryLevel.values()) {
            System.out.println(level);
            for (int i = 0; i < 30; i++) {
                Player player = PlayerCreator.createPlayer(GlobalPosition.FORWARD, level);
                System.out.println(player.nameWithPositionAndAverage());
            }
        }
    }

//    @Test
    public void testCreateLeagues() throws Exception {
        System.out.println(DataLoader.loadLeagues("config.xml"));
    }

    

    private League createLeague() {
        int teamsCount = 16;
        League league = new League("league1", teamsCount);
        for (int i = 0; i < teamsCount; i++) {
            Team team = new Team("team" + (i + 1), 0);
            league.addTeam(team);
            for (int j = 0; j < Team.MAX_PLAYERS_COUNT; j++) {
                team.addPlayer(PlayerCreator.createYoungPlayerSimple());
            }
        }
        return league;
    }

}
