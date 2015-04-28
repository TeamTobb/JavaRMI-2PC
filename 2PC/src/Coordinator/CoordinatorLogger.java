package Coordinator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CoordinatorLogger {
    private String fileName;
    private FileWriter fileWriter;

    public CoordinatorLogger(String dbName) {
        this.fileName = dbName + ".json";
    }

    public void log(CoordinatorLog logItem) {
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
