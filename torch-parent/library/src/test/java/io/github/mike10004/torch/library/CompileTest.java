package io.github.mike10004.torch.library;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompileTest {

    private static final String PACKAGE_NAME = "torch-private";
    private static final String SYSPROP_EXPECTED_EXECUTABLE = "torch-deb.expectedThExecExecutable";

    @Test
    public void checkStuffExists() throws Exception {
        File dir = resolveTorchBuildProductRoot();
        assertTrue("not a directory: " + dir, dir.isDirectory());
        java.nio.file.Files.walk(dir.toPath())
                .filter(path -> path.toFile().isFile())
                .limit(10).forEach(System.out::println);
    }

    @Test
    public void thFileExecCommand() throws Exception {
        String expectedExecutable = System.getProperty(SYSPROP_EXPECTED_EXECUTABLE, "/usr/bin/" + PACKAGE_NAME + "/luajit");
        File thFile = resolveTorchBuildProductRoot().toPath().resolve("bin").resolve("th").toFile();
        assertTrue("th file exists", thFile.isFile());
        String execLine = Files.readAllLines(thFile.toPath()).stream().filter(line -> line.startsWith("exec "))
                .findFirst().orElseThrow(() -> new AssertionError("`exec` line not found in " + thFile));
        String commandInQuotes = execLine.split("\\s+")[1];
        String command = StringUtils.strip(commandInQuotes, "'");
        assertEquals("exec command", expectedExecutable, command);
    }

    public static File resolveTorchBuildProductRoot() throws IOException, URISyntaxException {
        URL resource = CompileTest.class.getResource("/torch-build-product.txt");
        if (resource == null) {
            throw new FileNotFoundException("classpath:/torch-build-product.txt");
        }
        File descriptorFile = new File(resource.toURI());
        String fileContents = java.nio.file.Files.readAllLines(descriptorFile.toPath(), StandardCharsets.US_ASCII).stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .findFirst().orElseThrow(() -> new IllegalStateException("file does not contain any non-whitespace: " + descriptorFile));
        File dir = new File(fileContents);
        return dir;
    }
}