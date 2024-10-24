package dev.muetzilla;

import java.util.Calendar;
import java.util.Date;

public class Session {
    private long playtimeInMs = 0;

    private Date sessionEndDate;

    private Date sessionStartDate;

    private int hours;
    private int minutes;

    private Calendar calenderStartTime;

    public Date getSessionStartDate() {
        return sessionStartDate;
    }

    public long getPlaytimeInMsCalculated() {
        return hours * 3_600_000L + minutes * 60_000L;
    }

    public void setSessionStartDate(Date sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public long getPlaytimeInMs() {
        return playtimeInMs;
    }

    public void setPlaytimeInMs(long playtimeInMs) {
        this.playtimeInMs = playtimeInMs;
    }

    public Date getSessionEndDate() {
        return sessionEndDate;
    }

    public void setSessionEndDate(Date sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }

    public Session(Date sessionStartDate, Date sessionEndDate, int hours, int minutes) {
        this.sessionStartDate = sessionStartDate;
        this.sessionEndDate = sessionEndDate;
        this.hours = hours;
        this.minutes = minutes;

        calenderStartTime = Calendar.getInstance();
        calenderStartTime.setTime(sessionStartDate);
    }

    public Calendar getCalenderStartTime() {
        return calenderStartTime;
    }

    public int getStartingTimeYear() {
        return calenderStartTime.get(Calendar.YEAR);
    }

    public void setCalenderStartTime(Calendar calenderStartTime) {
        this.calenderStartTime = calenderStartTime;
    }

    public Session() {
    }

    public Session(long playtimeInMs, Date sessionEndDate) {
        this.playtimeInMs = playtimeInMs;
        this.sessionEndDate = sessionEndDate;
    }

    @Override
    public String toString() {
        return "Session{" +
                "playtimeInMs=" + playtimeInMs +
                ", sessionEndDate=" + sessionEndDate +
                ", sessionStartDate=" + sessionStartDate +
                ", hours=" + hours +
                ", minutes=" + minutes +
                '}';
    }
}
