package rusfootballmanager;

import rusfootballmanager.forms.LoginForm;

public class Main {

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null, true);
        loginForm.setVisible(true);
        loginForm.getTrainer();
    }

}
