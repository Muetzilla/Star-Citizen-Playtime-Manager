package dev.muetzilla;

public class Playtime {
    int hoursPlayed;
    int minutesPlayed;

    public Playtime(int hoursPlayed, int minutesPlayed) {
        this.hoursPlayed = hoursPlayed;
        this.minutesPlayed = minutesPlayed;
    }

    public Playtime() {
    }

    public int getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(int hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }
}
