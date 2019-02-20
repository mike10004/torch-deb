package io.github.mike10004.torch.packaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Utils {

    private Utils() {}

    public static File resolveDebStagingDir() throws IOException {
        File buildDir = resolveBuildDir();
        File stageDir = new File(buildDir, "deb");
        if (!stageDir.isDirectory()) {
            throw new FileNotFoundException("stage directory not found: " + stageDir);
        }
        return stageDir;
    }

    public static File resolveBuildDir() throws IOException {
        File d = new File(System.getProperty("project.build.directory", new File(System.getProperty("user.dir"), "target").getAbsolutePath()));
        if (!d.isDirectory()) {
            throw new FileNotFoundException("directory " + d);
        }
        return d;
    }

    public static File resolveDebFile() throws IOException {
        File buildDir = resolveBuildDir();
        return Files.walk(buildDir.toPath(), 1)
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(f -> f.getName().toLowerCase().endsWith(".deb"))
                .findFirst().orElseThrow(() -> new FileNotFoundException("no .deb file in " + buildDir));
    }

    public static <T> List<T> toList(Iterable<T> items) {
        return StreamSupport.stream(items.spliterator(), false).collect(Collectors.toList());
    }

    public static <T> Set<T> toSet(Iterable<T> items) {
        return StreamSupport.stream(items.spliterator(), false).collect(Collectors.toSet());
    }

    public static <K, V> Map<K, V> immutableCopyOf(Map<K, V> map) {
        return Collections.unmodifiableMap(new HashMap<>(map));
    }

    public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> map, Function<V1, V2> transform) {
        Map<K, V2> result = new HashMap<>();
        map.forEach((k, v1) -> {
            result.put(k, transform.apply(v1));
        });
        return result;
    }
}
