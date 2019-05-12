package ru.alexey_ovcharov.rusfootballmanager.career;

import ru.alexey_ovcharov.rusfootballmanager.common.DataLoader;
import ru.alexey_ovcharov.rusfootballmanager.entities.MessageConsumer;
import ru.alexey_ovcharov.rusfootballmanager.entities.player.Player;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.TransferStatus;

import javax.annotation.Nonnull;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @author Alexey
 */
public class Career implements MessageConsumer {

    private final List<Message> messages;
    private final String login;
    private LocalDate currentDate;
    private int experience;
    private String season;
    private List<League> leagues;
    private List<Tournament> tournaments;
    private Market transferMarket;
    private Team currentTeam;
    private Tournament currentTournament;

    public Career(String login) {
        this.login = login;
        messages = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        currentDate = LocalDate.of(calendar.get(Calendar.YEAR), Month.JANUARY, 14);
    }

    public boolean isNewCareer() {
        return leagues == null || transferMarket == null;
    }

    @Nonnull
    public List<League> getLeagues() throws Exception {
        if (leagues == null) {
            leagues = DataLoader.loadLeagues("config.xml");
        }
        return leagues;
    }

    public void createTransfers(Team myTeam, LocalDate date) {
        if (transferMarket == null) {
            transferMarket = Market.getInstance();
            leagues.stream()
                   .map(League::getTeams)
                   .flatMap(List::stream)
                   .forEach(team -> {
                       if (myTeam != team) {
                           team.addJuniors();
                       }
                       List<Player> players = team.getAllPlayers();
                       players.forEach(player -> transferMarket.addPlayer(player, team, TransferStatus.ON_CONTRACT));
                   });
            simulateTransfers(date, myTeam);
        }
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void createTournaments(LocalDate date) {
        tournaments = new ArrayList<>();
        if (leagues != null) {
            leagues.forEach(league -> tournaments.add(new Tournament(date, league)));
        }
    }

    public void simulateTransfers(LocalDate date, Team myTeam) {
        transferMarket.processOffers(date, myTeam);
        leagues.stream()
               .map(League::getTeams)
               .flatMap(Collection::stream)
               .filter(team -> team != myTeam)
               .forEach(team -> team.simulateTransfers(date));
    }

    public void nextYear() {
        leagues.stream()
               .map(League::getTeams)
               .flatMap(Collection::stream)
               .forEach(team -> {
                   if (team != currentTeam) {
                       team.nextYear(null);
                       List<Player> freeAgents = team.updateContractsAuto();
                       freeAgents.forEach(team::removePlayer);
                       transferMarket.addFreeAgents(freeAgents);
                   } else {
                       team.nextYear(this);
                   }
               });
        transferMarket.removeOldPlayers();

    }

    public void setCurrentTeam(Team currentTeam) {
        this.currentTeam = currentTeam;
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament aTournament : tournaments) {
                if (aTournament.containsTeam(currentTeam)) {
                    currentTournament = aTournament;
                    break;
                }
            }
        }
    }

    public static Career load(String login) {
        //currentDate = loadDate();
        return null;
    }

    public static Career newInstance(String login) {
        return new Career(login);
    }

    @Override
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public void clearMessages() {
        messages.clear();
    }

    public void removeMessage(int index) {
        if (index >= 0 && index < messages.size()) {
            messages.remove(index);
        }
    }

    public List<Message> getMessages() {
        return messages;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public Tournament getTournament() {
        return currentTournament;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
