package xml.utils;

import xml.consts.XmlTags;
import xml.models.Person;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ParserUtils {
    public static Person parsePersonElement(XMLStreamReader reader) {
        var person = new Person();
        for (int j = 0; j < reader.getAttributeCount(); j++) {
            var attrValue = StringUtils.normalizeSpace(reader.getAttributeValue(j));
            if (reader.getAttributeLocalName(j).equals(XmlTags.ID)) {
                person.setId(attrValue);
            } else {
                var divided = attrValue.split(" ");
                person.setName(divided[0]);
                person.setSurname(divided[1]);
            }
        }

        return person;
    }

    public static String parseSpouceElement(XMLStreamReader reader) {
        String spouseFullName = XmlTags.NONE;
        if (reader.getAttributeCount() == 0) {
            if (reader.hasText()) {
                spouseFullName = StringUtils.normalizeSpace(reader.getText());
            }
        } else {
            spouseFullName = StringUtils.normalizeSpace(reader.getAttributeValue(0));
        }

        return spouseFullName;
    }

    public static Collection<String> parseSiblingsElement(XMLStreamReader reader) {
        if (reader.getAttributeCount() == 0) {
            return new ArrayList();
        }

        var siblingIdsAttr = StringUtils.normalizeSpace(reader.getAttributeValue(0));
        var siblingIds = siblingIdsAttr.split(" ");

        return Arrays.asList(siblingIds);
    }

}
