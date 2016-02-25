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
        List<League> leagues;
        try {
            LoginForm loginForm = new LoginForm(null, true);
            loginForm.setVisible(true);
            User trainer = loginForm.getTrainer();
            if (trainer.getSettings().isNewCareer()) {
                CareerSettings careerSettings = new CareerSettings();
                trainer.setSettings(careerSettings);
                leagues = careerSettings.getLeagues();
                StartCareerForm startCareerForm = new StartCareerForm(null, true);
                startCareerForm.setData(leagues);
                startCareerForm.setVisible(true);
                Team team = startCareerForm.getChoosenTeam();
                trainer.setTeam(team);
            }

            ManageForm manageForm = new ManageForm(null, true);
            manageForm.setUser(trainer);
            manageForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString());
            System.exit(0);
        }

    }

}
