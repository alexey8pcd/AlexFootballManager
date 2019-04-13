package ru.alexey_ovcharov.rusfootballmanager.application;

import ru.alexey_ovcharov.rusfootballmanager.career.CareerSettings;
import ru.alexey_ovcharov.rusfootballmanager.career.User;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;
import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.League;
import ru.alexey_ovcharov.rusfootballmanager.represent.LoginForm;
import ru.alexey_ovcharov.rusfootballmanager.represent.ManageForm;
import ru.alexey_ovcharov.rusfootballmanager.represent.StartCareerForm;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
                StartCareerForm startCareerForm = new StartCareerForm(null, true);
                startCareerForm.setData(leagues);
                startCareerForm.setVisible(true);
                careerSettings.createTournaments(user.getCurrentDate());
                careerSettings.createTransfers();
                Team team = startCareerForm.getChoosenTeam();
                user.setTeam(team);
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
