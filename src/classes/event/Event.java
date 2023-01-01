package classes.event;

import java.time.LocalDateTime;

public abstract class Event {
    protected final int ID;
    protected String title;
    protected LocalDateTime start;
    protected int length;
    protected Unit unit;
    protected String[] participants;

    public Event(int ID, String title, LocalDateTime start, int length, Unit unit, String[] participants) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.length = length;
        this.unit = unit;
        this.participants = participants;
    }

    public int getId() {
        return ID;
    }

    public String getTitle() {
        return title;
    }


    public LocalDateTime getStart() {
        return start;
    }

    public abstract String getInformationString();

    public LocalDateTime getEndOfEvent() {
        return switch (this.unit) {
            case HOUR -> this.start.plusHours(this.length);
            case DAY -> this.start.plusDays(this.length);
            case MONTH -> this.start.plusMonths(this.length);
        };
    }
}
