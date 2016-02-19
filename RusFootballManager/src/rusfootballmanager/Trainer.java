package rusfootballmanager;

import rusfootballmanager.common.XMLFormatter;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import rusfootballmanager.entities.Team;
import rusfootballmanager.entities.League;

/**
 * @author Alexey
 */
public class Trainer {

    private String login;
    private Team team;
    private League league;
    private CareerSettings settings;
    private int experience;

    public static Trainer load(String login) {
        return null;
    }

    public static Trainer newInstance(String login) {
        return new Trainer(login);
    }

    public Trainer(String login) {
        this.login = login;
        settings = new CareerSettings();
    }

    public void save() {
        try {
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

            try (ZipOutputStream outputStream = new ZipOutputStream(
                    new FileOutputStream(new File(login)))) {
                outputStream.putNextEntry(new ZipEntry("save.dat"));
                XMLFormatter.elemenToStream(teamElement, outputStream);
                outputStream.flush();
            }
        } catch (Exception ex) {
            Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    

}
