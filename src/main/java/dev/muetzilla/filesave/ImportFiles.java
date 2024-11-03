package dev.muetzilla.filesave;

import dev.muetzilla.ConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImportFiles {
    private final String CONFIG_PATH = "config.json";
    public ConfigManager readConfigFile(){

        // Create a File object
        File file = new File(CONFIG_PATH);

        if (file.exists()) {
            System.out.println("File exists.");

//            try {
//                String content = new String(Files.readAllBytes(Paths.get(CONFIG_PATH)));
//
//                JSONObject jsonObject = new JSONObject(content);
//
//                String logbackupsLivePath = jsonObject.getString("logbackupsLivePath");
//                boolean ptuIsInstalled = jsonObject.getBoolean("ptuIsInstalled");
//
//
//                System.out.println("logbackupsLivePath: " + logbackupsLivePath);
//                System.out.println("ptuIsInstalled: " + ptuIsInstalled);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            try {
                ObjectMapper objectMapper = new ObjectMapper();

                ConfigManager config = objectMapper.readValue(new File(CONFIG_PATH), ConfigManager.class);
                config.setLogbackupsLivePath(config.getLogbackupsLivePath().replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\"));
                System.out.println("Path to Logbackups: " + config.getLogbackupsLivePath());
                System.out.println("PTU is installed: " + config.getPtuIsInstalled());
                return config;
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("File does not exist.");
        }
        return new ConfigManager();
    }
}
