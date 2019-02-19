package io.github.mike10004.torchtests;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class DebTest {

    @Test
    public void exists() throws URISyntaxException, IOException {
        File classpathRoot = new File(Placeholder.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        List<File> debFiles = java.nio.file.Files.walk(classpathRoot.toPath())
                .filter(p -> p.toFile().isFile())
                .map(Path::toFile)
                .filter(f -> f.getName().endsWith(".deb"))
                .collect(Collectors.toList());
        assertEquals("wrong number of deb files", 1, debFiles.size());
    }
}