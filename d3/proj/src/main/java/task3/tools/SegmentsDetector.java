package task3.tools;

import com.google.common.primitives.Bytes;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static task3.Constants.PERSON_START_TAG;

public class SegmentsDetector {

    private static final int BUFFER_SIZE = 1024;

    private final int segmentsCount;
    private final String fileName;
    private final byte[] tagBytes;

    private final List<Integer> segmentIndexes = new ArrayList<>();

    public SegmentsDetector(int segmentsCount, String fileName) {
        this.segmentsCount = segmentsCount;
        this.fileName = fileName;
        tagBytes = (PERSON_START_TAG).getBytes(StandardCharsets.UTF_8);

        init();
    }

    public List<Integer> getSegmentIndexes() {
        return segmentIndexes;
    }

    @SneakyThrows
    private void init() {
        @Cleanup
        var inputStream = new FileInputStream(fileName);

        var fileLength = inputStream.available();

        var currentPosition = 0;
        var buffer = new byte[BUFFER_SIZE];

        var segmentLength = fileLength / segmentsCount;
        for (var i = 0; i < segmentsCount; i++) {
            var bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);
            var tagPosition = Bytes.indexOf(buffer, tagBytes);
            if (tagPosition == -1) {
                throw new IOException("Cannot find tag");
            }

            segmentIndexes.add(currentPosition + tagPosition);
            currentPosition += bytesRead;
            currentPosition += inputStream.skip(segmentLength);
        }
    }
}