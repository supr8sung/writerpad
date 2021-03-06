package com.xebia.fs101.writerpad.model;

public class ReadingTime {
    private int mins;
    private int seconds;

    public void setMins(int mins) {

        this.mins = mins;
    }

    public void setSeconds(int seconds) {

        this.seconds = seconds;
    }
        public static ReadingTime calculate(String content, int averageTime) {

        ReadingTime readingTime = new ReadingTime();
        int totalWords = content.split("\\s+").length;
        readingTime.mins = totalWords / averageTime;
        double timeInSeconds = (double) (totalWords % averageTime) / averageTime * 60;
        readingTime.seconds = (int) timeInSeconds;


        return readingTime;
    }

    public int getMins() {

        return mins;
    }

    public int getSeconds() {

        return seconds;
    }
}
