package Cohort;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CohortLogger implements Serializable{
    private String fileName;
    private FileWriter fileWriter;

    public CohortLogger(String dbName) {
        this.fileName = dbName + ".json";
    }

    public void log(CohortLog logItem) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            if(logItem.getStatus().toString().equals("INIT")){
                this.fileWriter = new FileWriter(fileName, false);
                fileWriter.write('[');

            }else {
                this.fileWriter = new FileWriter(fileName, true);
                fileWriter.write(',');
            }
            mapper.writeValue(fileWriter, logItem);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<CohortLog> getLogItems(String fileName) {
/*
        ObjectMapper mapper = new ObjectMapper();
        List<CohortLog> logItems = null;
        try {
            logItems = mapper.readValue(new File(fileName), new TypeReference<List<CohortLog>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;


*/
        List<CohortLog> logItems = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            String jsonAsString = new String(encoded, Charset.defaultCharset());
            jsonAsString+="]";
            Collection<CohortLog> items = new ObjectMapper().readValue(jsonAsString, new TypeReference<Collection<CohortLog>>() { });
            logItems=new ArrayList<>(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logItems;
    }
}
