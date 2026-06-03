package hu.masterfield.appium;

import java.io.IOException;

public class Util {
    public static final String ADB_PATH = "C:/Users/master/AppData/Local/Android/Sdk/platform-tools/adb";

    public static void enablePointerPosition() {
        ProcessBuilder pb = new ProcessBuilder(ADB_PATH, "shell",
                "settings", "put", "system", "pointer_location", "1");
        Process pc = null;
        try {
            pc = pb.start();
            pc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void disablePointerPosition() {
        ProcessBuilder pb = new ProcessBuilder(ADB_PATH, "shell",
                "settings", "put", "system", "pointer_location", "0");
        Process pc = null;
        try {
            pc = pb.start();
            pc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
