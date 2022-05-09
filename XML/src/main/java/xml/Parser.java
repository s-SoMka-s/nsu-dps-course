package xml;

import xml.consts.XmlTags;
import xml.models.Person;
import xml.utils.ParserUtils;
import xml.utils.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class Parser {
    private final PeopleStore people = new PeopleStore();


    public Parser(String path) {
        parseFileToPersonList(path);

        people.ensureSuccess();
    }

    public Collection<Person> getPeople() {
        return people.getPeople();
    }


    private void parseFileToPersonList(String path) {
        try (var inputStream = new FileInputStream(path)) {
            var reader = XMLInputFactory.newInstance()
                    .createXMLStreamReader(inputStream);

            Person currentPerson = null;

            for (; reader.hasNext(); reader.next()) {
                var eventType = reader.getEventType();

                if (eventType == XMLStreamConstants.END_ELEMENT) {
                    var localName = reader.getLocalName();
                    if (localName.equals(XmlTags.PERSON)) {
                        people.add(currentPerson);
                        currentPerson = null;
                    }

                    continue;
                }

                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    var localName = reader.getLocalName();

                    switch (localName) {
                        case XmlTags.PERSON:
                            currentPerson = ParserUtils.parsePersonElement(reader);
                            break;

                        case XmlTags.ID:
                            var idAttr = reader.getAttributeValue(0).trim();
                            currentPerson.setId(idAttr);
                            break;

                        case XmlTags.FIRSTNAME:
                            if (reader.getAttributeCount() == 0) {
                                reader.next();
                                var firstNameText = reader.getText().trim();
                                currentPerson.setName(firstNameText);
                            } else {
                                var firstNameAttr = reader.getAttributeValue(0).trim();
                                currentPerson.setName(firstNameAttr);
                            }
                            break;


                        case XmlTags.SURNAME:
                            var surnameAttr = reader.getAttributeValue(0).trim();
                            currentPerson.setSurname(surnameAttr);
                            break;


                        case XmlTags.FAMILY_NAME:
                        case XmlTags.FAMILY:
                            reader.next();
                            var familyName = reader.getText().trim();
                            currentPerson.setSurname(familyName);
                            break;


                        case XmlTags.FIRST:
                            reader.next();
                            var firstText = reader.getText().trim();
                            currentPerson.setName(firstText);
                            break;


                        case XmlTags.GENDER:
                            String gender;
                            if (reader.getAttributeCount() == 0) {
                                reader.next();
                                gender = reader.getText().trim();
                            } else {
                                gender = reader.getAttributeValue(0).trim();
                            }
                            currentPerson.setGender(gender.toLowerCase(Locale.ROOT).contains("f") ? XmlTags.FEMALE : XmlTags.MALE);
                            break;


                        case XmlTags.SPOUCE:
                            var spouseFullName = ParserUtils.parseSpouceElement(reader);
                            if (!spouseFullName.equals(XmlTags.NONE)) {
                                currentPerson.setSpouseFullName(spouseFullName);
                            }
                            break;


                        case XmlTags.HUSBAND:
                            var husbandId = reader.getAttributeValue(0).trim();
                            currentPerson.setHusbandId(husbandId);
                            break;


                        case XmlTags.WIFE:
                            var wifeId = reader.getAttributeValue(0).trim();
                            currentPerson.setWifeId(wifeId);
                            break;


                        case XmlTags.PARENT:
                            var parentId = XmlTags.UNKNOWN;
                            if (reader.getAttributeCount() != 0) {
                                parentId = reader.getAttributeValue(0).trim();
                            }
                            if (!parentId.equals(XmlTags.UNKNOWN)) {
                                currentPerson.getParentIdSet().add(parentId);
                            }
                            break;


                        case XmlTags.FATHER:
                            reader.next();
                            var fatherFullName = StringUtils.normalizeSpace(reader.getText());
                            currentPerson.setFatherFullName(fatherFullName);
                            break;


                        case XmlTags.MOTHER:
                            reader.next();
                            var motherFullName = StringUtils.normalizeSpace(reader.getText());
                            currentPerson.setMotherFullName(motherFullName);
                            break;


                        case XmlTags.CHILD:
                            reader.next();
                            var childFullName = StringUtils.normalizeSpace(reader.getText());
                            currentPerson.getChildFullNameSet().add(childFullName);
                            break;


                        case XmlTags.SON:
                            var sonId = reader.getAttributeValue(0).trim();
                            currentPerson.getSonIdSet().add(sonId);
                            break;


                        case XmlTags.DAUGHTER:
                            var daughterId = reader.getAttributeValue(0).trim();
                            currentPerson.getDaughterIdSet().add(daughterId);
                            break;


                        case XmlTags.SIBLINGS:
                            var siblingIds = ParserUtils.parseSiblingsElement(reader);
                            currentPerson.getSiblingIdSet().addAll(siblingIds);
                            break;


                        case XmlTags.BROTHER:
                            reader.next();
                            var brotherFullName = StringUtils.normalizeSpace(reader.getText());
                            currentPerson.getBrotherFullNameSet().add(brotherFullName);
                            break;


                        case XmlTags.SISTER:
                            reader.next();
                            var sisterFullName = StringUtils.normalizeSpace(reader.getText());
                            currentPerson.getSisterFullNameSet().add(sisterFullName);
                            break;


                        case XmlTags.SIBLINGS_NUMBER:
                            var siblingsNumber = reader.getAttributeValue(0).trim();
                            try {
                                currentPerson.setSiblingsNumber(Integer.parseInt(siblingsNumber));
                            } catch (NumberFormatException e) {
                                System.out.println(e.getMessage());
                            }
                            break;


                        case XmlTags.CHILDREN_NUMBER:
                            var childrenNumber = reader.getAttributeValue(0).trim();
                            try {
                                currentPerson.setChildrenNumber(Integer.parseInt(childrenNumber));
                            } catch (NumberFormatException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                    }
                }
            }
            reader.close();
            System.out.println(people.size() + " - people list size");
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
