package classes.event;

import classes.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OnsiteEvent extends Event {
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, double length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public String getInformationString() {
        final DateTimeFormatter CUSTOM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return String.format("[%02d]    %-16s    %-16s    %-14s    %-6.2f    %-5s    %-3d", this.ID, this.title, this.location.getName(), this.start.format(CUSTOM_FORMAT), length, unit, this.participants.length);
    }

    public Location getLocation() {
        return location;
    }

}
