package bookstore.util;

import bookstore.dependesies.annotation.ConfigProperty;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigLoader {
    public static void load(Object obj) {
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
            if (annotation == null) continue;

            String fileName = annotation.configFileName();
            String propertyName =
                    annotation.propertyName().isEmpty()
                            ? clazz.getSimpleName() + "." + field.getName()
                            : annotation.propertyName();

            Properties props = loadProperties(fileName);

            String value = props.getProperty(propertyName);
            if (value == null) continue;

            Object convertedValue = convert(value, field.getType());

            field.setAccessible(true);
            try {
                field.set(obj, convertedValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is != null) props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    private static Object convert(String value, Class<?> type) {
        if (type.equals(int.class) || type.equals(Integer.class))
            return Integer.parseInt(value);

        if (type.equals(boolean.class) || type.equals(Boolean.class))
            return Boolean.parseBoolean(value);

        if (type.equals(double.class) || type.equals(Double.class))
            return Double.parseDouble(value);

        return value; // String or unknown type
    }
}
