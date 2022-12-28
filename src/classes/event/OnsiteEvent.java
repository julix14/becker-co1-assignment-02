package classes.event;

import classes.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OnsiteEvent extends Event {
    //Warum ist das hier nicht final?
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, int length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public String getInformation() {
        final DateTimeFormatter CUSTOM_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return String.format("[%02d]    %-16s    %-16s    %-14s    %-14s ", this.getID(), this.getTitle(), this.location.getName(), this.getStart().format(CUSTOM_FORMAT), this.getEndOfEvent().format(CUSTOM_FORMAT));
    }

    public Location getLocation() {
        return location;
    }

}
