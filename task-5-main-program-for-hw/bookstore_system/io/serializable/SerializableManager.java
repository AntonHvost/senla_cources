package bookstore_system.io.serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class SerializableManager {

    private static final String DATA_FILE = "bookstore_data.json";
    private final ObjectMapper objectMapper;

    public SerializableManager() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ApplicationState loadState() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("Файл данных не найден! Запускаем программу без данных.");
            return new ApplicationState();
        }
        try {
            return objectMapper.readValue(file, ApplicationState.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ApplicationState();
        }
    }

    public void saveState(ApplicationState state) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(DATA_FILE), state);
            System.out.println("Состояние успешно сохранено");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
