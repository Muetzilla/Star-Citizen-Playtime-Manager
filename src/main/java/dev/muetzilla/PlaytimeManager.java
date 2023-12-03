package dev.muetzilla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaytimeManager {

    private long totalPlaytime = 0;

    private String path = "";

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    Date lastDate;

    public PlaytimeManager() {

    }

    private int longestSession = 0;
    private Date longestSessionDate;

    public ArrayList<Session> getAllSessions() {
        return allSessions;
    }

    private ArrayList<Session> allSessions = new ArrayList<>();

    public PlaytimeManager(String path) {
        this.path = path;
    }

    public void searchThroughLines(BufferedReader reader) throws IOException {
        // Compile the regular expression pattern
        Pattern pattern = Pattern.compile("^<(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}).*");

        // Read each line of the file
        String line;
        ArrayList<Date> datesOneFile = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            // Check if the line matches the pattern
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                // If the line matches the pattern, print it
                Date tempDate = extractDate(line);
                datesOneFile.add(tempDate);
            }
        }
        Date startDate = datesOneFile.get(0);
        Date endDate = datesOneFile.get(datesOneFile.size() - 1);
        long playtimeOneFile = endDate.getTime() - startDate.getTime();

        long hours = TimeUnit.MILLISECONDS.toHours(playtimeOneFile);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(playtimeOneFile)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(playtimeOneFile));
        System.out.println("Your Session on the " + endDate + " was " + hours + " hours " + minutes + " minutes");
        allSessions.add(new Session(startDate, endDate, (int) hours, (int) minutes));
        if (hours * 60 + minutes >= longestSession) {
            longestSession = (int) (hours * 60 + minutes);
            longestSessionDate = endDate;
        }
        totalPlaytime += playtimeOneFile;

        lastDate = endDate;

        // Close the file
        reader.close();
    }

    public void getFiles() {
        // Create a File object for the folder
        File folder = new File(path);

        // Get a list of all the files in the folder
        File[] files = folder.listFiles();

        // Iterate through the list of files
        for (File file : files) {
            // Open the file
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                searchThroughLines(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String parentDir = folder.getParent();
        getCurrentGameSessionTime(parentDir);

    }

    public Date extractDate(String lineToExtractDateFrom) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Define the regular expression pattern
        Pattern pattern = Pattern.compile("<(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z)>");

        // Check if the string matches the pattern
        Matcher matcher = pattern.matcher(lineToExtractDateFrom);
        if (matcher.find()) {
            // If the string matches the pattern, extract the date
            String dateString = matcher.group(1);
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return date;
        }
        try {
            throw new Exception("String does not match the pattern \"<(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z)>\"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Playtime convertMilliesForDisplay() {
        // Convert the time to hours and minutes
        long hours = TimeUnit.MILLISECONDS.toHours(totalPlaytime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalPlaytime)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalPlaytime));
        Playtime playtime = new Playtime((int) hours, (int) minutes);

        System.out.println("Your Playtime is: " + hours + " hours " + minutes + " minutes");
        return playtime;
    }

    public void getCurrentGameSessionTime(String path) {

        File searchDirectory = new File(path);
        File[] logFiles = searchDirectory.listFiles((dir, name) -> name.endsWith(".log"));

        // Print the file names
        for (File file : logFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                searchThroughLines(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Your longest session was " + (int) longestSession / 60 + " hours and " + longestSession % 60 + " minutes on " + longestSessionDate);
    }

    public long getTotalPlaytime() {
        return totalPlaytime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
