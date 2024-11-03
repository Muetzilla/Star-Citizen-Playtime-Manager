package dev.muetzilla;


import dev.muetzilla.filesave.ImportFiles;

public class Main {

    public Main() {
        ImportFiles importFiles = new ImportFiles();
        System.out.println((importFiles.readConfigFile().toString()));
        MainFrame mf = new MainFrame(importFiles.readConfigFile());
        mf.setSize(600, 400);
    }

    public static void main(String[] args) {
        new Main();
    }


}
