package dev.muetzilla;

public class ConfigManager {
    private String logbackupsLivePath = "";
    private boolean ptuIsInstalled = false;


    public ConfigManager(String logbackupsLivePath, Boolean ptuIsInstalled) {
        this.logbackupsLivePath = logbackupsLivePath;
        this.ptuIsInstalled = ptuIsInstalled;
    }

    public ConfigManager() {
    }

    public String getLogbackupsLivePath() {
        return logbackupsLivePath;
    }

    public void setLogbackupsLivePath(String logbackupsLivePath) {
        this.logbackupsLivePath = logbackupsLivePath;
    }

    public boolean getPtuIsInstalled() {
        return ptuIsInstalled;
    }

    public void setPtuIsInstalled(boolean ptuIsInstalled) {
        this.ptuIsInstalled = ptuIsInstalled;
    }
    public String buildConfigJSON(){
        return "{" +
                "\"logbackupsLivePath\": \"" +  logbackupsLivePath + "\"," +
                "\"ptuIsInstalled\": " + ptuIsInstalled +
                "}";
    }

    @Override
    public String toString() {
        return "ConfigManager{" +
                "logbackupsLivePath='" + logbackupsLivePath + '\'' +
                ", ptuIsInstalled=" + ptuIsInstalled +
                '}';
    }
}
