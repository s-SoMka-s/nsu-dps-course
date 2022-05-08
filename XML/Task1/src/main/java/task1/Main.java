package task1;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class Main {
  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    var factory = SAXParserFactory.newInstance();
    var saxParser = factory.newSAXParser();
    var handler = new PeopleHandler();

    var file = new File("people.xml");
    saxParser.parse(file, handler);
  }
}
