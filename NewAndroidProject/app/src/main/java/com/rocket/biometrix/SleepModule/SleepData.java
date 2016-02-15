package com.rocket.biometrix.SleepModule;

/**
 * Created by TJ on 11/30/2015.
 */
public class SleepData
{
    //Different pieces of data gathered by the sleep module
    private String startTime;
    private String duration;
    private int sleepQuality;
    private String notes;
    private String healthStatus;

    //Sets the minimum and the maximum for sleep quality.
    public static final int minQuality = 1;
    public static final int maxQuality = 10;

    /**
     * Instantiates a SleepData object with the passed in data
     * @param start A string for the start time of the sleep
     * @param length A string for the length of the sleep
     * @param quality An integer corresponding to quality. Is bounded by minQuality and maxQuality.
     * @param status A string to describe health status
     * @param userNotes A string for user notes.
     */
    public SleepData(String start, String length, int quality, String status, String userNotes)
    {
        setStartTime(start);
        setDuration(length);
        setNotes(userNotes);
        setSleepQuality(quality);
        setHealthStatus(status);
    }

    /**
     * Instantiates a SleepData object with the passed in data
     * @param start A string for the start time of the sleep
     * @param length A string for the length of the sleep
     * @param quality An integer corresponding to quality. Is bounded by minQuality and maxQuality.
     * @param status A string to describe health status
     */
    public SleepData(String start, String length, int quality, String status)
    {
        setStartTime(start);
        setDuration(length);
        setNotes("");
        setSleepQuality(quality);
        setHealthStatus(status);
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(int sleepQuality) {
        //Ensures that no invalid values are given
        if (sleepQuality < minQuality)
        {
            this.sleepQuality = minQuality;
        }
        else if (sleepQuality > maxQuality)
        {
            this.sleepQuality = maxQuality;
        }
        else
        {
            this.sleepQuality = sleepQuality;
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}
