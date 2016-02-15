package rusfootballmanager;

import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;

/**
 * @author Alexey
 */
public class XMLFormatter {

    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

    public static void elemenToStream(Element element, OutputStream outputStream)
            throws TransformerException {
        DOMSource source = new DOMSource(element);
        Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
        transformer.transform(source, new StreamResult(outputStream));
    }

    public static String elementToString(Element element)
            throws TransformerException {
        DOMSource source = new DOMSource(element);
        Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
        StringWriter stringWriter = new StringWriter();
        transformer.transform(source, new StreamResult(stringWriter));
        return stringWriter.toString();
    }
}
