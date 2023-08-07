package dev.muetzilla;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
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

        JButton calcualtePlaytimeButton = new JButton("Calcualte Play");
        calcualtePlaytimeButton.setBackground(new Color(100, 100, 100));
        calcualtePlaytimeButton.setBorder(new LineBorder(new Color(100, 100, 100), 2));
        calcualtePlaytimeButton.setForeground(new Color(200, 200, 200));


        northPanel.add(pathInputField, BorderLayout.WEST);
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
            }
        });

        calcualtePlaytimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = pathInputField.getText();
                PlaytimeManager playtimeManager = new PlaytimeManager();
                playtimeManager.setPath(path);
                playtimeManager.getFiles();
                playtimeManager.getTotalPlaytime();
                Playtime playtime = playtimeManager.convertMilliesForDisplay();
                displayPlaytimeTextLIVE.setText("Your playtime on the LIVE servers is:  " + playtime.getHoursPlayed() + " hours " + playtime.getMinutesPlayed() + " minutes");

                String ptuPATH = path.replace("LIVE", "PTU");
                PlaytimeManager pm = new PlaytimeManager();
                pm.setPath(ptuPATH);
                pm.getFiles();
                pm.getTotalPlaytime();
                Playtime p = pm.convertMilliesForDisplay();
                displayPlaytimeTextPTU.setText("Your playtime on the PTU servers is:  " + p.getHoursPlayed() + " hours " + p.getMinutesPlayed() + " minutes");

                Playtime bothServersPlaytime = new Playtime();
                if (playtime.getMinutesPlayed() + p.getMinutesPlayed() >= 60) {
                    int newMinutesOfPlaytime = (playtime.getMinutesPlayed() + p.getMinutesPlayed()) % 60;
                    bothServersPlaytime.setHoursPlayed(playtime.getHoursPlayed() + p.getHoursPlayed() + 1);
                    bothServersPlaytime.setMinutesPlayed(newMinutesOfPlaytime);
                } else {
                    bothServersPlaytime.setMinutesPlayed(playtime.getMinutesPlayed() + p.getMinutesPlayed());
                    bothServersPlaytime.setHoursPlayed(playtime.getHoursPlayed() + p.getHoursPlayed());
                }
                displayPlaytimeTextTotalTime.setText("Your overall playtime is:  " + bothServersPlaytime.getHoursPlayed() + " hours " + bothServersPlaytime.getMinutesPlayed() + " minutes");

            }
        });

    }
}
