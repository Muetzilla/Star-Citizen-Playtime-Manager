package dev.muetzilla;

import dev.muetzilla.filesave.ExportPlaytime;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private boolean usePTUPlaytime = false;
    private Map<String, Integer> playtimeMap = new HashMap<>();

    private ArrayList<Session> allLiveSessions = new ArrayList<>();
    private ArrayList<Session> allPTUSessions = new ArrayList<>();
    private Date lastDate;

    public MainFrame() {
        super("Star Citizen Playtime Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        JTextField pathInputField = new JTextField();
        pathInputField.setText("ENTER YOUR PATH FOR THE LIVE LOGBACKUPS HERE");
        pathInputField.setBackground(new Color(64, 64, 64));
        pathInputField.setBorder(new LineBorder(new Color(190, 190, 190), 1));
        pathInputField.setForeground(new Color(200, 200, 200));

        JButton calculatePTUTimeButton = new JButton("PTU is installed: " + usePTUPlaytime);
        calculatePTUTimeButton.setBackground(new Color(100, 100, 100));
        calculatePTUTimeButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        calculatePTUTimeButton.setForeground(new Color(200, 200, 200));

        JButton calcualtePlaytimeButton = new JButton("Calcualte Playtime");
        calcualtePlaytimeButton.setBackground(new Color(100, 100, 100));
        calcualtePlaytimeButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        calcualtePlaytimeButton.setForeground(new Color(200, 200, 200));


        northPanel.add(pathInputField, BorderLayout.CENTER);
        northPanel.add(calculatePTUTimeButton, BorderLayout.WEST);
        northPanel.add(calcualtePlaytimeButton, BorderLayout.EAST);
        northPanel.setBackground(new Color(64, 64, 64));
        add(northPanel, BorderLayout.NORTH);


        GridLayout gl = new GridLayout(0, 1);
        southPanel.setLayout(gl);

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

        JButton exportButton = new JButton("Export Playtime");
        exportButton.setBackground(new Color(100, 100, 100));
        exportButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        exportButton.setForeground(new Color(200, 200, 200));
        southPanel.add(exportButton);

        southPanel.setBackground(new Color(64, 64, 64));
        add(southPanel, BorderLayout.CENTER);

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("EXPORT");
                ExportPlaytime ep = new ExportPlaytime();
                calculatePlaytime(pathInputField);
//                StringBuilder s = new StringBuilder();
//                for (Session allLiveSession : allLiveSessions) {
//                    s.append(allLiveSession);
//                }

                ep.createAndSaveJsonFile("starcitizenPlaytime.json", buildJSONString());
            }
        });

        calculatePTUTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usePTUPlaytime = !usePTUPlaytime;
                calculatePTUTimeButton.setText("PTU is installed: " + usePTUPlaytime);
//                createDialog();

            }
        });

        calcualtePlaytimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePlaytime(pathInputField);
                displayPlaytimeTextLIVE.setText("Your playtime on the LIVE servers is:  " + playtimeMap.get("livePlaytimeHours") + " hours " + playtimeMap.get("livePlaytimeMinutes") + " minutes");
                int totalPlaytimeHours = playtimeMap.get("livePlaytimeHours");
                int totalPlaytimeMinutes = playtimeMap.get("livePlaytimeMinutes");

                if (usePTUPlaytime) {
                    displayPlaytimeTextPTU.setText("Your playtime on the PTU servers is:  " + playtimeMap.get("ptuPlaytimeHours") + " hours " + playtimeMap.get("ptuPlaytimeMinutes") + " minutes");
                    totalPlaytimeHours += playtimeMap.get("ptuPlaytimeHours");
                    totalPlaytimeMinutes += playtimeMap.get("ptuPlaytimeMinutes");
                    if (totalPlaytimeMinutes >= 60) {
                        totalPlaytimeMinutes = totalPlaytimeMinutes % 60;
                        totalPlaytimeHours++;
                    }


                }

                displayPlaytimeTextTotalTime.setText("Your overall playtime is:  " + totalPlaytimeHours + " hours " + totalPlaytimeMinutes + " minutes");
            }
        });

    }

    public void createDialog() {
        final JDialog dialog = new JDialog(this, "Select Locations", true);
        dialog.setSize(100, 100);
        dialog.setBackground(new Color(64, 64, 64));

        JButton calculatePTUTimeButton = new JButton("PTU is installed: " + usePTUPlaytime);
        calculatePTUTimeButton.setBackground(new Color(100, 100, 100));
        calculatePTUTimeButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        calculatePTUTimeButton.setForeground(new Color(200, 200, 200));

        JPanel jPanel = new JPanel();
        jPanel.add(calculatePTUTimeButton, BorderLayout.NORTH);

        dialog.getContentPane().add(jPanel);
        dialog.pack();
        dialog.setVisible(true);


    }

    public void calculatePlaytime(JTextField pathInputField) {
        String path = pathInputField.getText();
        PlaytimeManager playtimeManager = new PlaytimeManager();
        playtimeManager.setPath(path);
        playtimeManager.getFiles();
        Playtime playtime = playtimeManager.convertMilliesForDisplay();
        playtimeMap.put("livePlaytimeHours", playtime.hoursPlayed);
        playtimeMap.put("livePlaytimeMinutes", playtime.minutesPlayed);
        allLiveSessions = playtimeManager.getAllSessions();

        if (usePTUPlaytime) {
            String ptuPATH = path.replace("LIVE", "PTU");
            PlaytimeManager pm = new PlaytimeManager();
            pm.setPath(ptuPATH);
            pm.getFiles();
//            String eptuPATH = path.replace("LIVE", "EPTU");
//            System.out.println(eptuPATH);
//            pm.setPath(eptuPATH);
//            pm.getFiles();
            Playtime p = pm.convertMilliesForDisplay();
            playtimeMap.put("ptuPlaytimeHours", p.hoursPlayed);
            playtimeMap.put("ptuPlaytimeMinutes", p.minutesPlayed);
            allPTUSessions = pm.getAllSessions();

        }
        lastDate = playtimeManager.getLastDate();


    }

    public String buildJSONString() {
        StringBuilder buildingJsonString = new StringBuilder("[\n");
        for (Session allLiveSession : allLiveSessions) {
            buildingJsonString.append("{\n\"endDate\":\"").append(allLiveSession.getSessionEndDate()).append("\",\n\"timeInMs\":").append(allLiveSession.getPlaytimeInMsCalculated()).append("\n},\n");
        }
        buildingJsonString.deleteCharAt(buildingJsonString.length() - 1);
        buildingJsonString.append("\n]");
        return buildingJsonString.toString();
    }
}
