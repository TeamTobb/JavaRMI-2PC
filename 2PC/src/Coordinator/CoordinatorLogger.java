package Coordinator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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
            //overwrite data if status == init
            if(logItem.getStatus().toString().equals("INIT")){
                this.fileWriter = new FileWriter(fileName, false);
            }else{
                this.fileWriter = new FileWriter(fileName, true);
            }
            mapper.writeValue(fileWriter, logItem);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CoordinatorLog> getLogItems() {
        ObjectMapper mapper = new ObjectMapper();
        List<CoordinatorLog> logItems = null;
        try {
            logItems = mapper.readValue(new File("Coordinatorlog.json"), new TypeReference<List<CoordinatorLog>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;
    }
}