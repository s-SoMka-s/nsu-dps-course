package xml;

import org.xml.sax.SAXException;
import xml.consts.FileNames;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HtmlTransformer{

    public HtmlTransformer() throws SAXException, ParserConfigurationException, TransformerException, IOException {
        var docBuilderFactory = DocumentBuilderFactory.newInstance();
        var schemaFactory= SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        var schemaFile = new File(FileNames.SCHEMA_FILE_NAME);

        docBuilderFactory.setSchema(schemaFactory.newSchema(schemaFile));
        docBuilderFactory.setNamespaceAware(true);

        var doc = docBuilderFactory.newDocumentBuilder().parse(FileNames.DATA_STRICT_FILE_NAME);
        var styleSheet = new StreamSource(new File(FileNames.TRANSFORM_FILE_NAME));
        var tf = TransformerFactory.newInstance();
        var transformer = tf.newTransformer(styleSheet);

        transformer.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(FileNames.HTML_FILE_NAME)));

        System.out.println("Transformation to html is done");
    }
}
