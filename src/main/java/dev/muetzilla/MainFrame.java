package dev.muetzilla;

import dev.muetzilla.filesave.ExportFiles;
import dev.muetzilla.filesave.ImportFiles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class MainFrame extends JFrame {

    private boolean usePTUPlaytime = false;
    private  Map<String, Integer> playtimeMap = new HashMap<>();

    private ArrayList<Session> allLiveSessions = new ArrayList<>();
    private ArrayList<Session> allPTUSessions = new ArrayList<>();
    private Date lastDate;

    private final JDialog settingsDialog = new JDialog(this, "Settings");
    private final ConfigManager configManager;

    public MainFrame(ConfigManager configManager) {
        super("Star Citizen Playtime Calculator");

        this.configManager = configManager;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("logo.png")));
        getContentPane().setBackground(new Color(64, 64, 64));
        initFrame();
        setVisible(true);

    }

    /**
     * Setup for the basic components of the frame
     */
    private void initFrame() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());


        GridLayout gl = new GridLayout(0, 1);
        southPanel.setLayout(gl);

        JButton settingsButton = new JButton("Settings");
        settingsButton.setBackground(new Color(100, 100, 100));
        settingsButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        settingsButton.setForeground(new Color(200, 200, 200));

        JButton helpButton = new JButton("Help");
        helpButton.setBackground(new Color(100, 100, 100));
        helpButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        helpButton.setForeground(new Color(200, 200, 200));


        northPanel.add(settingsButton, BorderLayout.EAST);
        northPanel.add(helpButton, BorderLayout.WEST);
        northPanel.setBackground(new Color(64, 64, 64));
        add(northPanel, BorderLayout.NORTH);

        JLabel displayPlaytimeTextLIVE = new JLabel();
        displayPlaytimeTextLIVE.setBackground(new Color(64, 64, 64));
        displayPlaytimeTextLIVE.setForeground(new Color(200, 200, 200));
        displayPlaytimeTextLIVE.setOpaque(true);
        southPanel.add(displayPlaytimeTextLIVE);

        JLabel displayPlaytimeTextPTU = new JLabel();
        displayPlaytimeTextPTU.setBackground(new Color(64, 64, 64));
        displayPlaytimeTextPTU.setForeground(new Color(200, 200, 200));
        displayPlaytimeTextPTU.setOpaque(true);
        southPanel.add(displayPlaytimeTextPTU);

        JLabel displayPlaytimeTextTotalTime = new JLabel();
        displayPlaytimeTextTotalTime.setBackground(new Color(64, 64, 64));
        displayPlaytimeTextTotalTime.setForeground(new Color(200, 200, 200));
        displayPlaytimeTextTotalTime.setOpaque(true);
        southPanel.add(displayPlaytimeTextTotalTime);

        JLabel displayYearlyPlaytime = new JLabel();
        displayYearlyPlaytime.setBackground(new Color(64, 64, 64));
        displayYearlyPlaytime.setForeground(new Color(200, 200, 200));
        displayYearlyPlaytime.setOpaque(true);
        southPanel.add(displayYearlyPlaytime);
        southPanel.setBackground(new Color(64, 64, 64));
        add(southPanel, BorderLayout.CENTER);

        calculatePlaytime(configManager.getLogbackupsLivePath());
        displayPlaytimeTextLIVE.setText("Your playtime on the LIVE servers is:  " + playtimeMap.get("livePlaytimeHours") + " hours " + playtimeMap.get("livePlaytimeMinutes") + " minutes");
        int totalPlaytimeHours = playtimeMap.get("livePlaytimeHours");
        int totalPlaytimeMinutes = playtimeMap.get("livePlaytimeMinutes");

        if (configManager.getPtuIsInstalled()) {
            displayPlaytimeTextPTU.setText("Your playtime on the PTU servers is:  " + playtimeMap.get("ptuPlaytimeHours") + " hours " + playtimeMap.get("ptuPlaytimeMinutes") + " minutes");
            totalPlaytimeHours += playtimeMap.get("ptuPlaytimeHours");
            totalPlaytimeMinutes += playtimeMap.get("ptuPlaytimeMinutes");
            if (totalPlaytimeMinutes >= 60) {
                totalPlaytimeMinutes = totalPlaytimeMinutes % 60;
                totalPlaytimeHours++;
            }


        }

        displayPlaytimeTextTotalTime.setText("Your overall playtime is:  " + totalPlaytimeHours + " hours " + totalPlaytimeMinutes + " minutes");

        getYearlyPlaytime(displayYearlyPlaytime, new int[]{2021, 2022, 2023, 2024, 2025});
        ExportFiles ep = new ExportFiles();
        ep.createAndSaveFile("starcitizenPlaytime.json", buildJSONString(configManager.getPtuIsInstalled()));

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                styleDialog();

                settingsDialog.setSize(600, 400);

                settingsDialog.setVisible(true);
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    URI url = new URI("https://github.com/Muetzilla/Star-Citizen-Playtime-Manager/blob/main/README.md");
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(url);
                    } else {
                        System.out.println("Desktop browsing is not supported on this platform.");
                    }
                } catch (URISyntaxException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        settingsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog Closed, create config file!");
                updateConfigSettings();
            }
        });

        settingsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Dialog Closed, create config file!");
                updateConfigSettings();            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Dialog Closed, create config file!");
                updateConfigSettings();            }
        });
    }

    private void updateConfigSettings() {
        ExportFiles ep = new ExportFiles();
        ep.createAndSaveFile("config.json", configManager.buildConfigJSON());
    }

    public void styleDialog(){

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        JTextField pathInputField = new JTextField();
        pathInputField.setText(configManager.getLogbackupsLivePath().replace("\\\\", "\\"));
        pathInputField.setBackground(new Color(64, 64, 64));
        pathInputField.setBorder(new LineBorder(new Color(190, 190, 190), 1));
        pathInputField.setForeground(new Color(200, 200, 200));

        JButton isPTUInstalledButton = new JButton("PTU is installed: " + configManager.getPtuIsInstalled());
        isPTUInstalledButton.setBackground(new Color(100, 100, 100));
        isPTUInstalledButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        isPTUInstalledButton.setForeground(new Color(200, 200, 200));

        northPanel.add(pathInputField, BorderLayout.CENTER);
        northPanel.add(isPTUInstalledButton, BorderLayout.WEST);
        northPanel.setBackground(new Color(64, 64, 64));


        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.setBackground(new Color(64, 64, 64));
        settingsDialog.add(mainPanel);

        final String[][] InputFieldContent = {{""}};

        pathInputField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateText();
            }

            private void updateText() {
                InputFieldContent[0] = new String[]{pathInputField.getText()};
                String pathFromInputField= Arrays.toString(InputFieldContent[0]);
                //.substring(1, pathFromInputField.length() - 1)
                configManager.setLogbackupsLivePath(pathFromInputField.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\").substring(1, pathFromInputField.length() - 1));
                System.out.println("Updated content: " + pathFromInputField.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\").substring(1, pathFromInputField.length() - 1));
            }
        });

        isPTUInstalledButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configManager.setPtuIsInstalled(!configManager.getPtuIsInstalled());
                isPTUInstalledButton.setText("PTU is installed: " + configManager.getPtuIsInstalled());
            }
        });

    }

    public void getYearlyPlaytime(JLabel label, int[] years) {
        StringBuilder totalYealryPlaytimeString = new StringBuilder("<html>");
        for (int year : years) {
            int yearlyHoursPlayed = 0;
            int yealryminutesPlayed = 0;
            for (Session allLiveSession : allLiveSessions) {
                if (allLiveSession.getStartingTimeYear() == year) {
                    yearlyHoursPlayed += allLiveSession.getHours();
                    yealryminutesPlayed += allLiveSession.getMinutes();
                }
            }
            if (configManager.getPtuIsInstalled()) {
                for (Session allPTUSession : allPTUSessions) {
                    if (allPTUSession.getStartingTimeYear() == year) {
                        yearlyHoursPlayed += allPTUSession.getHours();
                        yealryminutesPlayed += allPTUSession.getMinutes();
                    }
                }
            }
            yearlyHoursPlayed += yealryminutesPlayed / 60;
            yealryminutesPlayed = yearlyHoursPlayed % 60;

            totalYealryPlaytimeString.append("Your Playtime in the year ").append(year).append(" was: ").append(yearlyHoursPlayed).append(" hours and ").append(yealryminutesPlayed).append(" minutes. <br>");
        }
        totalYealryPlaytimeString.append("</html>");
        label.setText(totalYealryPlaytimeString.toString());

    }



    public void calculatePlaytime(String path) {
        PlaytimeManager playtimeManager = new PlaytimeManager();
        playtimeManager.setPath(path);
        boolean correctPath = playtimeManager.getFiles();
        Playtime playtime = playtimeManager.convertMilliesForDisplay();
        playtimeMap.put("livePlaytimeHours", playtime.hoursPlayed);
        playtimeMap.put("livePlaytimeMinutes", playtime.minutesPlayed);
        allLiveSessions = playtimeManager.getAllSessions();

        if (configManager.getPtuIsInstalled()) {
            String ptuPATH = path.replace("LIVE", "PTU");
            PlaytimeManager pm = new PlaytimeManager();
            pm.setPath(ptuPATH);
            pm.getFiles();
            String eptuPATH = path.replace("LIVE", "EPTU");
            System.out.println(eptuPATH);
            pm.setPath(eptuPATH);
            pm.getFiles();
            Playtime p = pm.convertMilliesForDisplay();
            playtimeMap.put("ptuPlaytimeHours", p.hoursPlayed);
            playtimeMap.put("ptuPlaytimeMinutes", p.minutesPlayed);
            allPTUSessions = pm.getAllSessions();

        }

        lastDate = playtimeManager.getLastDate();


    }

    public String buildJSONString(boolean usePTUPlaytime) {
        StringBuilder buildingJsonString = new StringBuilder("[\n");
        for (Session allLiveSession : allLiveSessions) {
            buildingJsonString.append("{\n\"endDate\":\"").append(allLiveSession.getSessionEndDate()).append("\",\n\"timeInMs\":").append(allLiveSession.getPlaytimeInMsCalculated()).append("\n},\n");
        }
        if (usePTUPlaytime) {
            for (Session allPTUSession : allPTUSessions) {
                buildingJsonString.append("{\n\"endDate\":\"").append(allPTUSession.getSessionEndDate()).append("\",\n\"timeInMs\":").append(allPTUSession.getPlaytimeInMsCalculated()).append("\n},\n");
            }
        }
        buildingJsonString.deleteCharAt(buildingJsonString.length() - 2);
        buildingJsonString.append("\n]");
        return buildingJsonString.toString();
    }
}





//        JPanel northPanel = new JPanel();
//        northPanel.setLayout(new BorderLayout());
//
//        JPanel southPanel = new JPanel();
//        southPanel.setLayout(new BorderLayout());
//
//        JTextField pathInputField = new JTextField();
//        pathInputField.setText("F:\\Star Citizen\\StarCitizen\\LIVE\\logbackups");//ENTER YOUR PATH FOR THE LIVE LOGBACKUPS HERE
//        pathInputField.setBackground(new Color(64, 64, 64));
//        pathInputField.setBorder(new LineBorder(new Color(190, 190, 190), 1));
//        pathInputField.setForeground(new Color(200, 200, 200));
//
//        JButton calculatePTUTimeButton = new JButton("PTU is installed: " + usePTUPlaytime);
//        calculatePTUTimeButton.setBackground(new Color(100, 100, 100));
//        calculatePTUTimeButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
//        calculatePTUTimeButton.setForeground(new Color(200, 200, 200));
//
//        JButton settingsButton = new JButton("Calcualte Playtime");
//        settingsButton.setBackground(new Color(100, 100, 100));
//        settingsButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
//        settingsButton.setForeground(new Color(200, 200, 200));
//
//
//        northPanel.add(pathInputField, BorderLayout.CENTER);
//        northPanel.add(calculatePTUTimeButton, BorderLayout.WEST);
//        northPanel.add(settingsButton, BorderLayout.EAST);
//        northPanel.setBackground(new Color(64, 64, 64));
//        add(northPanel, BorderLayout.NORTH);
//
//
//        GridLayout gl = new GridLayout(0, 1);
//        southPanel.setLayout(gl);
//
//        JLabel displayPlaytimeTextLIVE = new JLabel();
//        displayPlaytimeTextLIVE.setBackground(new Color(64, 64, 64));
//        displayPlaytimeTextLIVE.setForeground(new Color(200, 200, 200));
//        displayPlaytimeTextLIVE.setOpaque(true);
//        southPanel.add(displayPlaytimeTextLIVE);
//
//        JLabel displayPlaytimeTextPTU = new JLabel();
//        displayPlaytimeTextPTU.setBackground(new Color(64, 64, 64));
//        displayPlaytimeTextPTU.setForeground(new Color(200, 200, 200));
//        displayPlaytimeTextPTU.setOpaque(true);
//        southPanel.add(displayPlaytimeTextPTU);
//
//        JLabel displayPlaytimeTextTotalTime = new JLabel();
//        displayPlaytimeTextTotalTime.setBackground(new Color(64, 64, 64));
//        displayPlaytimeTextTotalTime.setForeground(new Color(200, 200, 200));
//        displayPlaytimeTextTotalTime.setOpaque(true);
//        southPanel.add(displayPlaytimeTextTotalTime);
//
//        JLabel displayYearlyPlaytime = new JLabel();
//        displayYearlyPlaytime.setBackground(new Color(64, 64, 64));
//        displayYearlyPlaytime.setForeground(new Color(200, 200, 200));
//        displayYearlyPlaytime.setOpaque(true);
//        southPanel.add(displayYearlyPlaytime);
//        southPanel.setBackground(new Color(64, 64, 64));
//        add(southPanel, BorderLayout.CENTER);


//        calculatePTUTimeButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                usePTUPlaytime = !usePTUPlaytime;
//                calculatePTUTimeButton.setText("PTU is installed: " + usePTUPlaytime);
////                createDialog();
//
//            }
//        });
//
//        settingsButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                calculatePlaytime(pathInputField);
//                displayPlaytimeTextLIVE.setText("Your playtime on the LIVE servers is:  " + playtimeMap.get("livePlaytimeHours") + " hours " + playtimeMap.get("livePlaytimeMinutes") + " minutes");
//                int totalPlaytimeHours = playtimeMap.get("livePlaytimeHours");
//                int totalPlaytimeMinutes = playtimeMap.get("livePlaytimeMinutes");
//
//                if (usePTUPlaytime) {
//                    displayPlaytimeTextPTU.setText("Your playtime on the PTU servers is:  " + playtimeMap.get("ptuPlaytimeHours") + " hours " + playtimeMap.get("ptuPlaytimeMinutes") + " minutes");
//                    totalPlaytimeHours += playtimeMap.get("ptuPlaytimeHours");
//                    totalPlaytimeMinutes += playtimeMap.get("ptuPlaytimeMinutes");
//                    if (totalPlaytimeMinutes >= 60) {
//                        totalPlaytimeMinutes = totalPlaytimeMinutes % 60;
//                        totalPlaytimeHours++;
//                    }
//
//
//                }
//
//                displayPlaytimeTextTotalTime.setText("Your overall playtime is:  " + totalPlaytimeHours + " hours " + totalPlaytimeMinutes + " minutes");
//
//                getYearlyPlaytime(displayYearlyPlaytime, new int[]{2021, 2022, 2023, 2024, 2025});
//                ExportPlaytime ep = new ExportPlaytime();
//                ep.createAndSaveJsonFile("starcitizenPlaytime.json", buildJSONString(usePTUPlaytime));
//
//            }
//        });
