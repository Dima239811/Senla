//package Task8.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//public class AppConfig {
//    private static final String PROPERTIES_FILE = "config.properties";
//    private static final Properties properties = new Properties();
//
//    private static final int DEFAULT_STALE_MONTHS = 3;
//    private static final boolean DEFAULT_AUTO_CLOSE = true;
//
//    static {
//        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
//            if (input != null) {
//                properties.load(input);
//            } else {
//                System.out.println("Конфигурационный файл не найден. Используются значения по умолчанию.");
//            }
//        } catch (IOException e) {
//            System.out.println("Ошибка чтения конфигурации. Используются значения по умолчанию.");
//        }
//    }
//
//    public static int getStaleBookMonths() {
//        try {
//            return Integer.parseInt(properties.getProperty("book.stale.months",
//                    String.valueOf(DEFAULT_STALE_MONTHS)));
//        } catch (NumberFormatException e) {
//            System.out.println("Некорректное значение book.stale.months. Используется значение по умолчанию.");
//            return DEFAULT_STALE_MONTHS;
//        }
//    }
//
//    public static boolean isAutoCloseRequestsEnabled() {
//        return Boolean.parseBoolean(properties.getProperty("book.auto.close.requests",
//                String.valueOf(DEFAULT_AUTO_CLOSE)));
//    }
//}
