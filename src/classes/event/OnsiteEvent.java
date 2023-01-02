package classes.event;

import classes.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class OnsiteEvent extends Event {
    private Location location;


    public OnsiteEvent(int ID, String title, LocalDateTime start, double length, Unit unit, String[] participants, Location location) {
        super(ID, title, start, length, unit, participants);
        this.location = location;
    }

    public String[] getInformationArray() {
        final DateTimeFormatter CUSTOM_DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return new String[]{String.format("%02d", this.ID),
                this.title,
                this.location.getName(),
                this.start.format(CUSTOM_DATE_FORMAT),
                String.format("%-6.2f", length),
                String.valueOf(unit),
                Arrays.toString(this.participants)};
    }

    public Location getLocation() {
        return location;
    }

}
