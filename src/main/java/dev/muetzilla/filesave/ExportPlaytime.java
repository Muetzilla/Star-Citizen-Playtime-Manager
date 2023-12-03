package dev.muetzilla.filesave;

import java.io.FileWriter;
import java.io.IOException;

public class ExportPlaytime {
    public void createAndSaveJsonFile(String filePath, String stringToSave) {
        try {
            // Write the JSON object to a file
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(stringToSave);
            fileWriter.flush();
            fileWriter.close();

            System.out.println("JSON file created successfully at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing JSON file: " + e.getMessage());
        }
    }

}
