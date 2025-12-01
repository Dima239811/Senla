package Task7.util;

import Task7.model.AppState;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveState(AppState state, String filePath) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppState loadState(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return mapper.readValue(file, AppState.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
