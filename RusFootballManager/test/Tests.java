
import com.sun.xml.internal.bind.v2.util.XmlFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import rusfootballmanager.entities.League;
import rusfootballmanager.entities.Team;
import rusfootballmanager.school.YoungPlayerCreator;

/**
 *
 * @author Алексей
 */
public class Tests {

    public Tests() {
    }

    @Before
    public void setUp() {
        Logger.getLogger("log").setLevel(Level.FINE);
    }

    @Test
    public void testConvertToXml() throws ParserConfigurationException, IOException, TransformerException {
        int teamsCount = 16;
        League league = new League("league1", teamsCount);
        for (int i = 0; i < teamsCount; i++) {
            Team team = new Team("team" + (i + 1));
            league.addTeam(team);
            for (int j = 0; j < Team.MAX_PLAYERS_COUNT; j++) {
                team.addPlayer(YoungPlayerCreator.createYoungPlayer());
            }
        }
        Document document = DocumentBuilderFactory.newInstance().
                newDocumentBuilder().newDocument();
        String result = league.toXmlString(document);
        
//        Source domSource = new DOMSource();
//        Result fileResult = new StreamResult(new File("outputFile.xml"));
//        TransformerFactory factory = TransformerFactory.newInstance();
//        Transformer transformer = factory.newTransformer();
//        transformer.transform(domSource, fileResult);
        
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new FileWriter(new File("test.xml")))) {
            bufferedWriter.write(result);
            bufferedWriter.flush();
        }
    }

}
