package ru.alexey_ovcharov.rusfootballmanager.application;

import ru.alexey_ovcharov.rusfootballmanager.career.Career;
import ru.alexey_ovcharov.rusfootballmanager.career.Message;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.entities.transfer.Market;
import ru.alexey_ovcharov.rusfootballmanager.represent.LoginForm;
import ru.alexey_ovcharov.rusfootballmanager.represent.ManageForm;
import ru.alexey_ovcharov.rusfootballmanager.represent.StartCareerForm;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            LoginForm loginForm = new LoginForm(null, true);
            loginForm.setLocationRelativeTo(null);
            loginForm.setVisible(true);
            Career career = loginForm.getCareer();
            if (career == null) {
                return;
            }
            if (career.isNewCareer()) {
                List<League> leagues = career.getLeagues();
                LocalDate currentDate = career.getCurrentDate();

                StartCareerForm startCareerForm = new StartCareerForm(null, true);
                startCareerForm.setData(leagues);
                startCareerForm.setVisible(true);
                Team team = startCareerForm.getChoosenTeam();
                if (team == null) {
                    return;
                }

                career.createTournaments(currentDate);
                career.createTransfers(team, currentDate);
                career.setCurrentTeam(team);
                Market.getInstance().setMarketListener(offer -> {
                    if (offer.getFromTeam() == team) {
                        career.addMessage(new Message(
                                "Директор команды " + offer.getToTeam().getName(),
                                offer.getDate(),
                                "Трансфер игрока " + offer.getPlayer().getNameAbbrAndLastName(),
                                "Мы хотим " + offer.getOfferType().getDescription().toLowerCase() + " у вас игрока " +
                                        offer.getPlayer().getNameAbbrAndLastName()));
                    }

                });
            }
            ManageForm manageForm = new ManageForm(null, true);
            manageForm.setCareer(career);
            manageForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString());
            System.exit(0);
        }

    }

}
