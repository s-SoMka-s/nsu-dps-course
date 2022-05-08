package task1;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import task1.entities.Gender;
import task1.entities.Person;
import task1.parsers.PersonXmlParser;

public class PeopleHandler extends DefaultHandler {

    private String currentTagName;
    private Person currentPerson;
    private People people;
    private PersonXmlParser parser;

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println(this.people);
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr)
            throws SAXException {
        this.currentTagName = qName;
        var attributes = parseAttributes(attr);
        switch (qName) {
            case XmlElementNames.People:
                this.parsePeopleElement(attr);
                // parse people Element
                break;
            case XmlElementNames.Person:
                this.parsePersonElement(attributes);
                // set current person
                // create new or use existed (id or full name "name")
                break;
            case XmlElementNames.Gender:
                // set gender to current person
                this.parseGenderElement(attributes);
                break;
            case XmlElementNames.Firstname:
                // set firstName to current person
                this.parseFirstnameElement(attributes);
                break;
            case XmlElementNames.Surname:
                // set surname to current person
            case XmlElementNames.Family:
                // set surname to current person
                this.parseSurnameElement(attributes);
                break;
            case XmlElementNames.Wife:
            case XmlElementNames.Husband:
            case XmlElementNames.Spouce:
                this.parseSpouseElement(attributes);
                break;
            case XmlElementNames.Parent:
                break;
            case XmlElementNames.Daughter:
            case XmlElementNames.Son:
            case XmlElementNames.Siblings:
            case XmlElementNames.ChildrenNumber:
                break;
            case XmlElementNames.SiblingsNumber:
                break;
        }
    }


    private void parseFirstnameElement(ArrayList<XmlAttribute> attributes) {
        if (currentPerson.hasFirstName()) {
            return;
        }

        var valueAttr = attributes.stream().filter(a -> a.getName().equals(XmlElementAttributeNames.Value)).findFirst().orElse(null);
        if (valueAttr == null) {
            return;
        }

        var firstName = valueAttr.getValue();
        currentPerson.setFirstName(firstName);
    }

    private void parseSurnameElement(ArrayList<XmlAttribute> attributes) {
        if (currentPerson.hasSurname()) {
            return;
        }

        var valueAttr = attributes.stream().filter(a -> a.getName().equals(XmlElementAttributeNames.Value)).findFirst().orElse(null);
        if (valueAttr == null) {
            return;
        }

        var surname = valueAttr.getValue();
        currentPerson.setFirstName(surname);
    }

    private void parseGenderElement(ArrayList<XmlAttribute> attributes) {
        if (currentPerson.hasGender()) {
            return;
        }

        var valueAttr = attributes.stream().filter(a -> a.getName().equals(XmlElementAttributeNames.Value)).findFirst().orElse(null);
        if (valueAttr == null) {
            return;
        }

        var gender = valueAttr.getValue().equals("male") ? Gender.Male : Gender.Female;
        currentPerson.setGender(gender);
    }

    private void parseSpouseElement(ArrayList<XmlAttribute> attributes) {
        if (currentPerson.hasSpouse()) {
            return;
        }

        var valueAttr = attributes.stream().filter(a -> a.getName().equals(XmlElementAttributeNames.Value)).findFirst().orElse(null);
        if (valueAttr == null) {
            return;
        }

        var spouseFullName = valueAttr.getValue();
        if (spouseFullName.equals("none")) {
            return;
        }

        var spouse = people.firstOrNull(spouseFullName);
        var spouseGender = Gender.Unknown;
        if (currentTagName.equals(XmlElementNames.Wife)) {
            spouseGender = Gender.Female;
        } else if (currentTagName.equals(XmlElementNames.Husband)) {
            spouseGender = Gender.Male;
        }

        if (spouse == null) {
            var newPerson = new Person(spouseFullName, spouseGender);
            people.addPerson(newPerson);
            return;
        }

        currentPerson.setSpouse(spouse);
        spouse.setSpouse(currentPerson);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentTagName = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        //System.out.println("Characters: " + new String(ch, start, length));

    }

    private void parsePeopleElement(Attributes attr) {
        var value = attr.getValue(0);
        var count = Long.parseLong(value);
        this.people = new People(count);
    }

    private ArrayList<XmlAttribute> parseAttributes(Attributes attr) {
        var attrCount = attr.getLength();
        var res = new ArrayList<XmlAttribute>();
        for (var i = 0; i < attrCount; i++) {
            var name = attr.getLocalName(i).trim().toLowerCase();
            var value = attr.getValue(i).trim().toLowerCase();
            res.add(new XmlAttribute(name, value));
        }

        return res;
    }

    private void parsePersonElement(ArrayList<XmlAttribute> attributes) {

    }
}

