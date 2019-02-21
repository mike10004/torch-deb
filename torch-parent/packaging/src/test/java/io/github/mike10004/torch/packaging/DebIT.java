package io.github.mike10004.torch.packaging;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DebIT {

    @Test
    public void stagingDirNonempty() throws Exception {
        File stageDir = Utils.resolveDebStagingDir();
        Path usrBinDir = stageDir.toPath().resolve("usr").resolve("bin");
        if (!usrBinDir.toFile().isDirectory()) {
            throw new FileNotFoundException("not a directory: " + usrBinDir);
        }
        Files.walk(usrBinDir, 1)
                .map(Path::toFile)
                .filter(File::isFile)
                .forEach(file -> {
                    if (Files.isSymbolicLink(file.toPath())) {
                        try {
                            file = file.toPath().toRealPath().toFile();
                        } catch (IOException e) {
                            fail(e.toString());
                        }
                    }
                    assertTrue("file must be executable: " + file, file.canExecute());
                });
    }

    @Test
    public void debFileExists() throws Exception {
        File debFile = Utils.resolveDebFile();
        System.out.format("deb file: %s", debFile);
    }

    @Test
    public void debFileExtracts() throws Exception {
        File debFile = Utils.resolveDebFile();
        System.out.format("deb file: %s", debFile);
        // TODO really examine the file
    }


}
