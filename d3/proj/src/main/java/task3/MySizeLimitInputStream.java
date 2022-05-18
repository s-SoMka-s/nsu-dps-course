package task3;

import com.Ostermiller.util.SizeLimitInputStream;

import java.io.IOException;
import java.io.InputStream;

public class MySizeLimitInputStream extends SizeLimitInputStream {

    public MySizeLimitInputStream(InputStream in, long maxBytesToRead){
        super(in, maxBytesToRead);
    }

    @Override
    public long skip(long n) throws IOException {
        var skippedBytes = super.skip(n);
        bytesRead += skippedBytes;
        return skippedBytes;
    }
}