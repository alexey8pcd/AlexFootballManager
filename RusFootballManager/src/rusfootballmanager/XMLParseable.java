package rusfootballmanager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLParseable {

    public Element toXmlElement(Document document);
    public String toXmlString(Document document);
}