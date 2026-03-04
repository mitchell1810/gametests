package utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class AppConfigLoader {

    private final static String PROPERTIES_FILE_PATH = "src/test/resources/app.properties";
    private static Properties properties = new Properties();

    public AppConfigLoader() {
        properties = loadAppProperties();
    }

    private Properties loadAppProperties() {
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH);
            props.load(fis);
            fis.close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return props;
    }

    public Properties getProperties() {
        return properties;
    }

}
