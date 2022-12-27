package classes.event;

import java.time.LocalDateTime;

public class Event {
    private final int ID;
    private String title;
    private LocalDateTime start;
    private double length;
    private TimeUnit timeUnit;
    private String[] participants;

    public Event(int ID, String title, LocalDateTime start, double length, TimeUnit timeUnit, String[] participants) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.length = length;
        this.timeUnit = timeUnit;
        this.participants = participants;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public double getLength() {
        return length;
    }


    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

}
