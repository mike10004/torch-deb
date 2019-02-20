package io.github.mike10004.torch.packaging;

import org.junit.Test;

import java.io.File;

public class DebIT {

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
