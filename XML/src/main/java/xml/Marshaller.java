package xml;

import org.xml.sax.SAXException;
import xml.consts.FileNames;
import xml.models.Person;
import xml.xmltypes.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Marshaller {

    private final Collection<Person> people;
    private final Map<String, PersonXML> personXmlMap = new HashMap<>();

    public Marshaller(Collection<Person> persons) throws SAXException, JAXBException {
        this.people = persons;

        fillMap();

        var people = new PeopleXML() {{
            setPersonList(new ArrayList<>(personXmlMap.values()));
        }};

        marshal(people);

        System.out.println("All done!");
    }

    private void setHusband(Person person, PersonXML personXML) {
        if (person.getHusbandId() == null) {
            return;
        }
        personXML.setSpouse(new RefIdXML() {{
            setId(personXmlMap.get(person.getHusbandId()));
        }});
    }

    private void setWife(Person person, PersonXML personXML) {
        if (person.getWifeId() == null) {
            return;
        }
        personXML.setSpouse(new RefIdXML() {{
            setId(personXmlMap.get(person.getWifeId()));
        }});
    }

    private void setFather(Person person, PersonXML personXML) {
        if (person.getFatherId() == null) {
            return;
        }

        var parentsXML = personXML.getParents();
        if (parentsXML == null) {
            parentsXML = new ParentsXML();
            personXML.setParents(parentsXML);
        }
        parentsXML.setFather(new RefIdXML() {{
            setId(personXmlMap.get(person.getFatherId()));
        }});
    }

    private void setMother(Person person, PersonXML personXML) {
        if (person.getMotherId() == null) {
            return;
        }

        var parentsXML = personXML.getParents();
        if (parentsXML == null) {
            parentsXML = new ParentsXML();
            personXML.setParents(parentsXML);
        }
        parentsXML.setMother(new RefIdXML() {{
            setId(personXmlMap.get(person.getMotherId()));
        }});
    }

    private void setSons(Person person, PersonXML personXML) {
        if (person.getSonIdSet().isEmpty()) {
            return;
        }

        personXML.setSons(new SonsXML() {{
            List<RefIdXML> sonsXml = new ArrayList<>();
            for (String sonId : person.getSonIdSet()) {
                sonsXml.add(new RefIdXML() {{
                    setId(personXmlMap.get(sonId));
                }});
            }
            setRefs(sonsXml);
        }});
    }

    private void setDaughters(Person person, PersonXML personXML) {
        if (person.getDaughterIdSet().isEmpty()) {
            return;
        }
        personXML.setDaughters(new DaughtersXML() {{
            List<RefIdXML> daughtersXml = new ArrayList<>();
            for (String daughterId : person.getDaughterIdSet()) {
                daughtersXml.add(new RefIdXML() {{
                    setId(personXmlMap.get(daughterId));
                }});
            }
            setRefs(daughtersXml);
        }});
    }

    private void setBrothers(Person person, PersonXML personXML) {
        if (person.getBrotherIdSet().isEmpty()) {
            return;
        }

        personXML.setBrothers(new BrothersXML() {{
            List<RefIdXML> brothersXML = new ArrayList<>();
            for (String brotherId : person.getBrotherIdSet()) {
                brothersXML.add(new RefIdXML() {{
                    setId(personXmlMap.get(brotherId));
                }});
            }
            setRefs(brothersXML);
        }});

    }

    private void setSisters(Person person, PersonXML personXML) {
        if (person.getSisterIdSet().isEmpty()) {
            return;
        }
        personXML.setSisters(new SistersXML() {{
            List<RefIdXML> sistersXML = new ArrayList<>();
            for (String sisterId : person.getSisterIdSet()) {
                sistersXML.add(new RefIdXML() {{
                    setId(personXmlMap.get(sisterId));
                }});
            }
            setRefs(sistersXML);
        }});

    }

    private void fillMap() {
        for (var person : people) {
            personXmlMap.put(person.getId(), new PersonXML());
        }

        for (var person : people) {
            var personXML = personXmlMap.get(person.getId());

            personXML.setId(person.getId());
            personXML.setName(person.getFullName());
            personXML.setGender(person.getGender());

            setHusband(person, personXML);
            setWife(person, personXML);

            setFather(person, personXML);
            setMother(person, personXML);

            setSons(person, personXML);
            setDaughters(person, personXML);

            setBrothers(person, personXML);
            setSisters(person, personXML);
        }
    }

    private void marshal(PeopleXML people) throws SAXException, JAXBException {
        var context = JAXBContext.newInstance(PeopleXML.class);
        var marshaller = context.createMarshaller();
        var schemaFactory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

        marshaller.setSchema(schemaFactory.newSchema(new File(FileNames.SCHEMA_FILE_NAME)));
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(people, new File(FileNames.DATA_STRICT_FILE_NAME));
    }
}
