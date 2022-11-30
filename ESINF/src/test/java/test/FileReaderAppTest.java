package test;

import app.domain.shared.FilesReaderApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileReaderAppTest {

    @Test
    public void testReadIrrigationDeviceFile() {
        Assertions.assertTrue(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.csv")));
        Assertions.assertFalse(FilesReaderApp.readIrrigationDeviceFile(new File("src/files/wateringController.txt")));
    }
}
