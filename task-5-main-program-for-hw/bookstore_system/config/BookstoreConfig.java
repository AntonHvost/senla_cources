package bookstore_system.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class BookstoreConfig {
    private static final String CONFIG_FILE = "app.properties";

    private final int unsoldBookMonth;
    private final boolean autoCompleteOrder;
    private final Path ioPath;

    private static BookstoreConfig instance;

    private BookstoreConfig() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Не найден файл конфигурации: " + CONFIG_FILE);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.unsoldBookMonth = Integer.parseInt(properties.getProperty("unsoldBookMonth"));
        this.autoCompleteOrder = Boolean.parseBoolean(properties.getProperty("autoCompleteOrder"));
        String ioPath = properties.getProperty("ioPath");
        this.ioPath = Paths.get(ioPath).normalize();
    }

    public static BookstoreConfig getInstance() {
        if (instance == null) {
            instance = new BookstoreConfig();
        }
        return instance;
    }

    public int getUnsoldBookMonth() {
        return unsoldBookMonth;
    }

    public boolean isAutoCompleteOrder() {
        return autoCompleteOrder;
    }

    public Path getIoDir() {
        return ioPath;
    }
}
