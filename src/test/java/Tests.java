
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.common.util.RoundRobinScheduleGenerator;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.GlobalPosition;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.MasteryLevel;
import ru.alexey_ovcharov.rusfootballmanager.entities.school.PlayerCreator;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Opponents;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Schedule;

/**
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
        League league = createLeague();
        LocalDate start = LocalDate.of(2016, Month.AUGUST, 1);
        LocalDate end = LocalDate.of(2017, Month.JUNE, 31);
        LocalDate holidaysStart = LocalDate.of(2016, Month.NOVEMBER, 15);
        LocalDate holidaysEnd = LocalDate.of(2017, Month.MARCH, 1);
        Schedule tournamentSchedule = new Schedule(start, end,
                holidaysStart, holidaysEnd, 2, league.getTeams());
        Map<LocalDate, List<Opponents>> schedule = tournamentSchedule.getSchedule();
        schedule.forEach((date, pairs) -> {
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

    @Test
    public void testRRScheduling() {
        int teamsCount = 16;
        int loopsCount = 2;
        List<List<Map.Entry<Integer, Integer>>> list = RoundRobinScheduleGenerator.roundRobinScheduling(
                teamsCount, loopsCount);
        Set<Map.Entry<Integer, Integer>> controlSet = new HashSet<>();
        for (List<Map.Entry<Integer, Integer>> entries : list) {
            for (Map.Entry<Integer, Integer> entry : entries) {
                boolean res = controlSet.add(entry);
                if (!res) {
                    Assert.fail();
                }
            }
        }
    }


}
