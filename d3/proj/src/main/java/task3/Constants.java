package task3;

public class Constants {
    public static final String FILENAME = "src\\main\\resources\\people_strict.xml";
    public static final String SQL_FILENAME = "src\\main\\resources\\script.sql";

    public static final String XML_FILE_PREFIX = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:people xmlns:ns2=\"http://fit.nsu.ru/people\">";
    public static final String XML_FILE_POSTFIX = "</ns2:people>";

    public static final String PERSON_START_TAG = "<person";

    public static final String XML_PERSON_TAG_NAME = "person";
    public static final String XML_SPOUSE_TAG_NAME = "spouse";
    public static final String XML_FATHER_TAG_NAME = "father";
    public static final String XML_MOTHER_TAG_NAME = "mother";

    public static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/people_db";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "postgres";
}
