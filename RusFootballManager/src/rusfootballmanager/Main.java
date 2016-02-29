package rusfootballmanager;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Team;
import rusfootballmanager.forms.LoginForm;
import rusfootballmanager.forms.ManageForm;
import rusfootballmanager.forms.StartCareerForm;

public class Main {

    public static void main(String[] args) {

        try {
            LoginForm loginForm = new LoginForm(null, true);
            loginForm.setVisible(true);
            User user = loginForm.getTrainer();
            if (user == null) {
                return;
            }
            if (user.getSettings().isNewCareer()) {
                CareerSettings careerSettings = new CareerSettings();
                user.setSettings(careerSettings);
                List<League> leagues = careerSettings.getLeagues();
                StartCareerForm startCareerForm = new StartCareerForm(null, true);
                startCareerForm.setData(leagues);
                startCareerForm.setVisible(true);
                careerSettings.createTournaments(user.getCurrentDate());
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
