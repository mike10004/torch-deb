package io.github.mike10004.torch.packaging;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DebTest {

    @Test
    public void usrBinFilesExecutable() throws Exception {
        File stageDir = Utils.resolveDebStagingDir();
        Path usrBinDir = stageDir.toPath().resolve("usr").resolve("bin");
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

}