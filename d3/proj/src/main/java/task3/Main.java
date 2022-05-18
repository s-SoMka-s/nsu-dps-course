package task3;

import lombok.SneakyThrows;
import task3.tools.SegmentsDetector;

import static task3.Constants.FILENAME;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        var threadsCount = 5;
        var segmentIndexes = new SegmentsDetector(threadsCount, FILENAME).getSegmentIndexes();
        var uploader = new XmlUploader(FILENAME, segmentIndexes);
        uploader.upload();
    }
}