package config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

public class Configurator {

    public void configure(Object configObject) {
        Class<?> clazz = configObject.getClass();
        Properties properties;

        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation == null) continue;

            field.setAccessible(true);

            String fileName = annotation.ConfigFileName();
            String key = annotation.propertyName();

            if (key.isEmpty()) key = field.getName();

            properties = loadProperties(fileName);

            String value = properties.getProperty(key);
            if (value == null) {
                System.err.println("Property " + key + " not found");
                continue;
            }

            try {
                Object convertValue = convertValue(value, field.getType());
                field.set(configObject, convertValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void configureObjects(Set<Object> obj) {
        for (Object object : obj) {
            configure(object);
        }
    }

    private Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new RuntimeException("Не найден файл конфигурации: " + fileName);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    private Object convertValue(Object value, Class<?> type) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value.toString().trim());
        }  else if (type == Boolean.class  || type == boolean.class) {
            return Boolean.parseBoolean(value.toString().trim());
        }  else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value.toString().trim());
        }   else if (type == Long.class || type == long.class) {
            return Long.parseLong(value.toString().trim());
        }   else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value.toString().trim());
        }
        return value;
    }
}
