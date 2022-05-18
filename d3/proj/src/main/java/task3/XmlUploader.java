package task3;

import lombok.Cleanup;
import lombok.SneakyThrows;
import task3.entities.Gender;
import task3.entities.Person;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static task3.Constants.*;

public class XmlUploader {
    private final String fileName;
    private final List<Integer> segmentIndexes;

    List<Thread> threads;

    public XmlUploader(String fileName, List<Integer> segmentIndexes) {
        this.fileName = fileName;
        this.segmentIndexes = segmentIndexes;

        recreateTable();
        initThreads();
    }

    @SneakyThrows
    public void upload() {
        for (var thread : this.threads) {
            thread.start();
            thread.join();
        }
    }

    @SneakyThrows
    private void recreateTable() {
        @Cleanup
        var connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        @Cleanup
        var statement = connection.createStatement();

        var script = Files.readString(Path.of(SQL_FILENAME));
        statement.execute(script);
    }

    @SneakyThrows
    private void initThreads() {
        threads = new ArrayList<>();

        for (var i = 0; i < segmentIndexes.size() - 1; i++) {
            final var j = i;
            threads.add(new Thread(() -> runThread(j)));
        }
    }

    @SneakyThrows
    private void runThread(int threadNumber) {
        @Cleanup
        var prefixInputStream = new ByteArrayInputStream(XML_FILE_PREFIX.getBytes());

        @Cleanup
        var postfixInputStream = new ByteArrayInputStream(XML_FILE_POSTFIX.getBytes());

        @Cleanup
        var peopleInputStream = new FileInputStream(fileName);

        @Cleanup
        var peopleLimitedInputStream =
                new MySizeLimitInputStream(peopleInputStream, segmentIndexes.get(threadNumber + 1));

        @Cleanup
        var sequenceInputStream = new SequenceInputStream(
                Collections.enumeration(new ArrayList<>() {{
                    add(prefixInputStream);
                    add(peopleLimitedInputStream);
                    add(postfixInputStream);
                }})
        );

        var skipped = peopleLimitedInputStream.skip(segmentIndexes.get(threadNumber));
        if (skipped != segmentIndexes.get(threadNumber)) {
            throw new IOException("Skip failed");
        }

        @Cleanup
        var xmlStreamReader =
                XMLInputFactory.newInstance().createXMLStreamReader(sequenceInputStream);

        @Cleanup
        var connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        runThread(xmlStreamReader, connection);
    }

    @SneakyThrows
    private void runThread(XMLStreamReader reader, Connection statement) {
        var person = new Person();

        while (reader.hasNext()){
            if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
                reader.next();
                continue;
            }

            switch (reader.getLocalName()) {
                case XML_PERSON_TAG_NAME -> {

                    if (person.getId() != null) {
                        insertPerson(statement, person);
                        person.clear();
                    }

                    setPersonAttributes(reader, person);
                }
                case XML_SPOUSE_TAG_NAME -> person.setSpouseId(Integer.parseInt(reader.getAttributeValue(0).substring(1)));
                case XML_FATHER_TAG_NAME -> person.setFatherId(Integer.parseInt(reader.getAttributeValue(0).substring(1)));
                case XML_MOTHER_TAG_NAME -> person.setMotherId(Integer.parseInt(reader.getAttributeValue(0).substring(1)));
            }

            reader.next();
        };
    }

    private void setPersonAttributes(XMLStreamReader reader, Person person) {
        for (int j = 0; j < reader.getAttributeCount(); j++) {
            var attrValue = reader.getAttributeValue(j);
            switch (reader.getAttributeLocalName(j)) {
                case "id" -> person.setId(Integer.parseInt(attrValue.substring(1)));
                case "name" -> person.setName(attrValue);
                case "gender" -> person.setGender(attrValue.startsWith("M") ? Gender.Male : Gender.Female);
            }
        }
    }

    @SneakyThrows
    private void insertPerson(Connection connection, Person person) {
        var script = connection.prepareStatement(
                "INSERT INTO people (id, name, gender, fatherId, motherId, spouseId)" +
                        "VALUES (?, ?, CAST(? AS Gender), ?, ?, ?)"
        );

        script.setInt(1, person.getId());
        script.setString(2, person.getName());
        script.setString(3, person.getGender().name());
        script.setObject(4, person.getFatherId());
        script.setObject(5, person.getMotherId());
        script.setObject(6, person.getSpouseId());

        var res = script.executeUpdate();

        System.out.println(person);
    }
}