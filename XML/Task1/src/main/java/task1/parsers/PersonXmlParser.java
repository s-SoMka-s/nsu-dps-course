package task1.parsers;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import task1.XmlAttribute;
import task1.XmlElementAttributeNames;
import task1.entities.Person;

public class PersonXmlParser {

  private Person person;

  public PersonXmlParser() {
    person = new Person();
  }

  private ArrayList<XmlAttribute> parseAttributes(Attributes attr) {
    var attrCount = attr.getLength();
    var res = new ArrayList<XmlAttribute>();
    for (var i = 0; i < attrCount; i++) {
      var name = attr.getLocalName(i);
      var value = attr.getValue(i);
      res.add(new XmlAttribute(name, value));
    }

    return res;
  }

  public void parseFirstnameElement(String tagName, Attributes raw) {
    var attributes = this.parseAttributes(raw);
    for (var attr : attributes) {
      var attrName = attr.getName();
      switch (attrName) {
        case XmlElementAttributeNames.Value:
          this.person.setFirstName(attr.getValue());
      }
    }
  }

  public void parseSurnameElement(String tagName, Attributes raw) {
    var attributes = this.parseAttributes(raw);
    for (var attr : attributes) {
      var attrName = attr.getName();
      switch (attrName) {
        case XmlElementAttributeNames.Value:
          this.person.setSurname(attr.getValue());
      }
    }
  }

  public Person getPerson() {
    return this.person;
  }
}

