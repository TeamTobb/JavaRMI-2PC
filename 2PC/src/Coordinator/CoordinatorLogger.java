package Coordinator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoordinatorLogger{
    private String fileName;
    private FileWriter fileWriter;

    public CoordinatorLogger(String dbName) {
        this.fileName = dbName + ".json";
    }

    public void log(CoordinatorLog logItem) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            if(logItem.getStatus().toString().equals("INIT")){
                this.fileWriter = new FileWriter(fileName, false);
                fileWriter.write('[');
            }else{
                this.fileWriter = new FileWriter(fileName, true);
                fileWriter.write(',');
            }
            mapper.writeValue(fileWriter, logItem);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CoordinatorLog> getLogItems(Long id) {
        ArrayList<CoordinatorLog>logItems = null;

        try {
            byte[] encoded = Files.readAllBytes(Paths.get("errorlogs/" + id + ".json"));
            String jsonAsString = new String(encoded, Charset.defaultCharset());
            jsonAsString+="]";
            Collection<CoordinatorLog> items = new ObjectMapper().readValue(jsonAsString, new TypeReference<Collection<CoordinatorLog>>() { });
            logItems=new ArrayList<>(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;
    }

    public List<CoordinatorLog> getLogItems() {
        ArrayList<CoordinatorLog>logItems = null;

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            String jsonAsString = new String(encoded, Charset.defaultCharset());
            jsonAsString+="]";
            Collection<CoordinatorLog> items = new ObjectMapper().readValue(jsonAsString, new TypeReference<Collection<CoordinatorLog>>() { });
            logItems=new ArrayList<>(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;
    }

    public void errorLog(Long id){
        try{
            Files.createDirectory(Paths.get("errorlogs"));
        } catch(IOException e){
        }
        Path from = Paths.get("Coordinatorlog.json");
        Path to = Paths.get("errorlogs/" + id + ".json");
        try {
            Files.copy(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}