//package Utilities;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//public class ConfigReader {
//    private static Properties props;
//
//    static {
//        try {
//            props = new Properties();
//            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
//            props.load(fis);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load config.properties", e);
//        }
//    }
//
//    public static String get(String key) {
//        return props.getProperty(key);
//    }
//}
package Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties props;

    static {
        try {
            props = new Properties();
            // Using System.getProperty("user.dir") ensures path works on any OS/IDE
            String path = System.getProperty("user.dir") + "/src/main/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties. Check the path: " + e.getMessage());
        }
    }

    public static String get(String key) {
        String value = props.getProperty(key);
        if (value != null) {
            return value;
        }
        throw new RuntimeException("Key '" + key + "' not found in config.properties");
    }
}