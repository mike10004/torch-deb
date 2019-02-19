package io.github.mike10004.torchtests;

import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LibTest {

    @Test
    public void exists() throws Exception {
        URL resource = getClass().getResource("/torch-library.zip");
        assertNotNull("resource at /torch-library.zip", resource);
        File zipFile = new File(resource.toURI());
        assertTrue("file has nonzero length", zipFile.length() > 0);
    }
}
