package ru.alexey_ovcharov.rusfootballmanager.career;

import ru.alexey_ovcharov.rusfootballmanager.entities.tournament.Tournament;
import ru.alexey_ovcharov.rusfootballmanager.common.util.XMLFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.alexey_ovcharov.rusfootballmanager.entities.team.Team;

/**
 * @author Alexey
 */
public class User {

    private String login;
    private Team team;
    private Date currentDate;
    private Tournament tournament;
    private CareerSettings settings;
    private List<Message> messages;
    private int experience;

    public static User load(String login) {
        //currentDate = loadDate();
        return null;
    }

    public static User newInstance(String login) {
        return new User(login);
    }

    public User(String login) {
        this.login = login;
        settings = new CareerSettings();
        messages = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 14);
        currentDate = calendar.getTime();
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
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

    public void setLogin(String login) {
        this.login = login;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        List<Tournament> tournaments = settings.getTournaments();
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament aTournament : tournaments) {
                if (aTournament.containsTeam(team)) {
                    tournament = aTournament;
                    break;
                }
            }
        }
    }

    public Tournament getTournament() {
        return tournament;
    }

    public CareerSettings getSettings() {
        return settings;
    }

    public void setSettings(CareerSettings settings) {
        this.settings = settings;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void save() {
        try (ZipOutputStream outputStream = new ZipOutputStream(
                new FileOutputStream(new File(login)))) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder;
            documentBuilder = builderFactory.newDocumentBuilder();
            DOMImplementation domi = documentBuilder.getDOMImplementation();
            Document document = domi.createDocument(null, null, null);
            Element loginElement = document.createElement("login");
            loginElement.setAttribute("name", login);
            loginElement.setAttribute("experience", String.valueOf(experience));
            document.appendChild(loginElement);
            Element teamElement = team.toXmlElement(document);
            loginElement.appendChild(teamElement);
            Element settingsElement = settings.toXmlElement(document);
            loginElement.appendChild(settingsElement);
            outputStream.putNextEntry(new ZipEntry("save.dat"));
            XMLFormatter.elemenToStream(teamElement, outputStream);
            outputStream.flush();
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
