package io.github.mike10004.torch.packaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public interface ByteSource {

    static ByteSource wrap(byte[] array) {
        return () -> new ByteArrayInputStream(array);
    }

    default byte[] read() throws IOException {
        try (InputStream in = openStream()) {
            return ByteStreams.toByteArray(in);
        }
    }

    InputStream openStream() throws IOException;
}
