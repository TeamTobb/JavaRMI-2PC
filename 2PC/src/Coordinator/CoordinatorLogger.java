package Coordinator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CoordinatorLogger implements Serializable{
    private String fileName;
    private FileWriter fileWriter;

    public CoordinatorLogger(String dbName) {
        this.fileName = dbName + ".json";
    }

    public CoordinatorLogger(){
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

    public List<CoordinatorLog> getLogItems() {
        ObjectMapper mapper = new ObjectMapper();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}