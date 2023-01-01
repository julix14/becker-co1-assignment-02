package classes.event;

import classes.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OnsiteEvent extends Event {
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, int length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public String getInformationString() {
        final DateTimeFormatter CUSTOM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return String.format("[%02d]    %-16s    %-16s    %-14s    %-14s    %-3d", this.ID, this.title, this.location.getName(), this.start.format(CUSTOM_FORMAT), this.getEndOfEvent().format(CUSTOM_FORMAT), this.participants.length);
    }

    public Location getLocation() {
        return location;
    }

}
