package ru.alexey_ovcharov.rusfootballmanager.application;

import ru.alexey_ovcharov.rusfootballmanager.career.CareerSettings;
import ru.alexey_ovcharov.rusfootballmanager.career.Message;
import ru.alexey_ovcharov.rusfootballmanager.career.User;
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
            loginForm.setVisible(true);
            User user = loginForm.getTrainer();
            if (user == null) {
                return;
            }
            CareerSettings userCareerSettings = user.getSettings();
            if (userCareerSettings.isNewCareer()) {
                CareerSettings careerSettings = new CareerSettings();
                user.setSettings(careerSettings);
                List<League> leagues = careerSettings.getLeagues();
                LocalDate currentDate = user.getCurrentDate();

                StartCareerForm startCareerForm = new StartCareerForm(null, true);
                startCareerForm.setData(leagues);
                startCareerForm.setVisible(true);
                Team team = startCareerForm.getChoosenTeam();

                careerSettings.createTournaments(currentDate);
                careerSettings.createTransfers(team, currentDate);
                user.setTeam(team);
                Market.getInstance().setMarketListener(offer -> {
                    if (offer.getFromTeam() == team) {
                        user.addMessage(new Message(
                                "Директор команды " + offer.getToTeam().getName(),
                                offer.getDate(),
                                "Трансфер игрока " + offer.getPlayer().getNameAbbrAndLastName(),
                                "Мы хотим " + offer.getOfferType().getDescription().toLowerCase() + " у вас игрока " +
                                        offer.getPlayer().getNameAbbrAndLastName()));
                    }

                });
            }
            ManageForm manageForm = new ManageForm(null, true);
            manageForm.setUser(user);
            manageForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString());
            System.exit(0);
        }

    }

}
