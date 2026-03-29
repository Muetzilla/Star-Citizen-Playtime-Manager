package dev.muetzilla;

public class ConfigManager {
    private String logbackupsLivePath = "";
    private boolean ptuIsInstalled = false;


    public ConfigManager(String logbackupsLivePath, Boolean ptuIsInstalled) {
        this.logbackupsLivePath = logbackupsLivePath;
        this.ptuIsInstalled = ptuIsInstalled;
        formatString();
    }

    public ConfigManager() {
        formatString();
    }

    public String getLogbackupsLivePath() {
        return logbackupsLivePath;
    }

    public void setLogbackupsLivePath(String logbackupsLivePath) {
        this.logbackupsLivePath = logbackupsLivePath;
        formatString();
    }

    public boolean getPtuIsInstalled() {
        return ptuIsInstalled;
    }

    public void setPtuIsInstalled(boolean ptuIsInstalled) {
        this.ptuIsInstalled = ptuIsInstalled;
    }

    /**
     * Formats the String to a fitting format.
     *
     * Escapes all \ in the path by adding a \\ instead.
     *
     * Removes the [] at the start and the end which result from converting the Array to a String after reading the String from the input filed.
     */
    public void formatString(){
        logbackupsLivePath = logbackupsLivePath.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\");
        logbackupsLivePath = logbackupsLivePath.replaceAll("[\\[\\]]", "");
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
