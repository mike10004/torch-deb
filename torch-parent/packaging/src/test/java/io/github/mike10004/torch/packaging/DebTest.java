package io.github.mike10004.torch.packaging;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotEquals;

public class DebTest {

    @Test
    public void stagingDirNonempty() throws Exception {
        File stageDir = Utils.resolveDebStagingDir();
        long numFiles = Files.walk(stageDir.toPath()).map(Path::toFile)
                .filter(File::isFile)
                .count();
        assertNotEquals("expect nonzero file count in " + stageDir, 0, numFiles);
    }

}