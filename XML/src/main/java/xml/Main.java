package xml;

import org.xml.sax.SAXException;
import xml.consts.FileNames;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws SAXException, JAXBException, ParserConfigurationException, IOException, TransformerException {
        // task_1
        var people = new Parser(FileNames.INPUT_FILE_NAME).getPeople();

        // task_2
        new Marshaller(people);

        // task_3
        new HtmlTransformer();
    }
}
