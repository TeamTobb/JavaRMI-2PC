package Cohort;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.io.IOException;

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
}
