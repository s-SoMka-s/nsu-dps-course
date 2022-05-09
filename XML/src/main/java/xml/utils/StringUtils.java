package xml.utils;

public class StringUtils {
    public static String normalizeSpace(String str) {
        return str.trim().replaceAll("\\s+", " ");
    }
}
