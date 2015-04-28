package Cohort;

import Coordinator.CoordinatorLog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CohortLogger {
    private String fileName;
    private FileWriter fileWriter;

    public CohortLogger(String dbName) {
        this.fileName = dbName + ".json";
    }

    public void log(CohortLog logItem) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            this.fileWriter = new FileWriter(fileName, true);
            mapper.writeValue(fileWriter, logItem);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CohortLog> getLogItems(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        List<CohortLog> logItems = null;
        try {
            logItems = mapper.readValue(new File(fileName), new TypeReference<List<CohortLog>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;
    }
}
