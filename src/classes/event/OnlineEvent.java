package classes.event;

import classes.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OnlineEvent extends Event {
    private final Location LOCATION;

    public OnlineEvent(int ID, String title, LocalDateTime start, int length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.LOCATION = location;
    }

    public String getInformationString() {
        final DateTimeFormatter CUSTOM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return String.format("[%02d]    %-16s    %-16s    %-14s    %-14s    %-3d", this.getId(), this.getTitle(), this.LOCATION.getName(), this.getStart().format(CUSTOM_FORMAT), this.getEndOfEvent().format(CUSTOM_FORMAT), this.getParticipants().length);
    }

}
