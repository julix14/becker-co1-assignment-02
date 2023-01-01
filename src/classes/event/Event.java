package classes.event;

import java.time.LocalDateTime;

public abstract class Event {
    private final int ID;
    private String title;
    private LocalDateTime start;
    private int length;
    private Unit unit;
    private String[] participants;

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

    public String[] getParticipants() {
        return participants;
    }

}
