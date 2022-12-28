package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.Unit;
import helper.ArrayHelper;
import helper.EventHelperService;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventPlanner {
    private Location[] locations;
    private final UserInputService USERINPUTSERVICE = new UserInputService();
    private final ValidationService VALIDATIONSERVICE = new ValidationService();

    private int eventCount = 0;

    private Event[] events = new Event[0];

    //Ich glaube Mohammed wollte die initialisierung immer im constructur
    public EventPlanner(int locationCount) {
        //locations = setupLocations(locationCount + 1);
        locations = demoLocations();
    }

    private Location[] setupLocations(int locationCount){
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", locationCount - 1);
        this.locations = new Location[locationCount];
        for (int i = 0; i < locationCount - 1; i++) {
            String name = USERINPUTSERVICE.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i + 1));
            int maxCapacity = VALIDATIONSERVICE.getValidIntFromUser(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        locations[locationCount - 1] = new Location("Online", -1);
        return locations;
    }

    private Location[] demoLocations() {
        Location[] locations = new Location[3];
        locations[0] = new Location("Online", -1);
        locations[1] = new Location("Room 1", 10);
        locations[2] = new Location("Room 2", 20);

        events = ArrayHelper.add(events, new OnlineEvent(1, "Online Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));
        events = ArrayHelper.add(events, new OnlineEvent(2, "Online Event 2", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[0]));

        events = ArrayHelper.add(events, new OnsiteEvent(3, "Onsite Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[1]));

        events = ArrayHelper.add(events, new OnsiteEvent(5, "Onsite Event 3", LocalDateTime.of(2021, 1, 1, 10, 0), 1, Unit.HOUR, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(6, "Onsite Event 4", LocalDateTime.of(2021, 2, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(7, "Onsite Event 5", LocalDateTime.of(2021, 3, 1, 10, 0), 1, Unit.DAY, new String[]{"John", "Jane"}, locations[2]));

        return locations;
    }

    public void createNewEvent(){
        System.out.println("Create new event");
        // Get name of new Event
        String name = USERINPUTSERVICE.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = VALIDATIONSERVICE.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        //Warum long? Int sollte doch eigentlich ausreichen oder?
        // Get length of new Event
        int length = VALIDATIONSERVICE.getValidIntFromUser("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = VALIDATIONSERVICE.getValidIntInRangeFromUser("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        Unit unit = Unit.values()[timeUnitPlace - 1];

        // Select Location of new Event
        System.out.println("Please select the location of the event:");
        Location location = locationSelector(locations);

        // Check if location is available
        // Disable Check if Online Event#

        //Irgendwie ist das wie du locationAvailable gelöst hast ziemlich kompliziert. Würde das in eine do while schleife tuen.
        //Irgendwie doppelt sich da auch einiges. Vielleicht kannst du das noch simpler machen, sodass man es besser versteht
        boolean locationAvailable;
        if (location.getName().equals("Online")){
            locationAvailable = true;
        } else {
            //Diese Zeile ist einfach nur mies zum lesen. Würde das aufteilen
            locationAvailable = EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, unit).length == 0;
        }

        while(!locationAvailable){
            Location[] freeLocations = getFreeLocationsOnDate(startDate, length, unit);
            if (freeLocations.length != 0){
                System.out.println("The selected location is not available on the selected date. Please select another location:");
                location = locationSelector(freeLocations);
            } else {
                System.out.println("There are no free locations on the selected date. Please select another date:");
                startDate = VALIDATIONSERVICE.getValidLocalDateTimeFromUser("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");
            }
            if (location.getName().equals("Online")){
                locationAvailable = true;
            } else {
                locationAvailable = EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, unit).length == 0;
            }
        }

        // Add participants
        int participantsCount;
        if (location.getName().equals("Online")){
            participantsCount = VALIDATIONSERVICE.getValidIntFromUser("Please enter the number of participants: ");
        } else {
            participantsCount = VALIDATIONSERVICE.getValidIntInRangeFromUser("Please enter the number of participants: ", 1, location.getMaxCapacity());
        }

        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = USERINPUTSERVICE.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i + 1));
        }

        // Create Event and add to events-list
        //Kannst du stark vereinfachen
        /*
        Event event;
        if (location.getName().equals("Online")) {
            event = new OnlineEvent(eventCount, name, startDate, length, unit, participants, location);

        } else {
            event = new OnsiteEvent(eventCount, name, startDate, length, unit, participants, location);
        }
        events = ArrayHelper.add(events, event);
        eventCount++;
         */

        Event event;
        if (location.getName().equals("Online")) {
            events = ArrayHelper.add(events, new OnlineEvent(eventCount++, name, startDate, length, unit, participants, location));
            return;
        }

        events = ArrayHelper.add(events, new OnsiteEvent(eventCount++, name, startDate, length, unit, participants, location));

        System.out.println("Event created successfully!");
    }

    public void showAllEvents() {
        printEvents(events);
        System.out.println();
    }

    public void showEventsByTitle() {
        //Würde die Variable irgendwie umbennen zu searchedNameLowerCase oder das in zwei Zeilen schreiben
        String searchedName = USERINPUTSERVICE.getStringFromUserWithMessage("Please enter the name of the searched event: ").toLowerCase();

        Event[] selectedEvents = new Event[0];
        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(searchedName)) {
                selectedEvents = ArrayHelper.add(selectedEvents, event);
            }
        }
        printEvents(selectedEvents);
    }

    public void showEventsByLocation() {
        Location location = locationSelector(locations);
        printEvents(EventHelperService.eventsOnLocation(events, location));
    }

    public void showEventsByParticularDate() {
        LocalDate searchedDate = VALIDATIONSERVICE.getValidLocalDateFromUser("Please enter the date of the searched events: (DD.MM.YYYY)");
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            allEvents = ArrayHelper.addAll(
                    EventHelperService.eventsWhileDuration(
                            EventHelperService.eventsOnLocation(events, location),
                            searchedDate.atStartOfDay(),
                            1,
                            Unit.DAY),
                    allEvents);
        }
        printEvents(allEvents);
    }

    public void showMostUsedLocation(){
        Location mostUsedLocation = locations[0];
        int mostUsedLocationCount = 0;
        for (Location location : locations) {
            if (EventHelperService.eventsOnLocation(events, location).length > mostUsedLocationCount){
                mostUsedLocation = location;
                mostUsedLocationCount = EventHelperService.eventsOnLocation(events, location).length;
            }
        }
        System.out.printf("%nThe most used location is %s with %d events.%n", mostUsedLocation.getName(), mostUsedLocationCount);
    }

    public void showLeastUsedLocation(){
        Location leastUsedLocation = locations[0];
        int leastUsedLocationCount = Integer.MAX_VALUE;
        for (Location location : locations) {
            if (EventHelperService.eventsOnLocation(events, location).length < leastUsedLocationCount){
                leastUsedLocation = location;
                leastUsedLocationCount = EventHelperService.eventsOnLocation(events, location).length;
            }
        }
        System.out.printf("%nThe least used location is %s with %d events.%n", leastUsedLocation.getName(), leastUsedLocationCount);

    }


    private void printEvents(Event[] events){
        // Set formatting constants for printing
        String WHITE_UNDERLINED = "\033[4m";
        String RESET = "\033[0m";
        //Nicht final also name convention
        DateTimeFormatter CUSTOMFORMAT = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        // Sort events by ID
        events = ArrayHelper.sortById(events);

        //Print events as table
        if (events.length == 0) {
            System.out.println("No events found.");
        } else {
            System.out.printf("%d Events found:%n", events.length);
            System.out.printf(WHITE_UNDERLINED + "  %-4s    %-16s    %-10s    %-14s    %-14s" + RESET, "ID", "Title", "Location", "Start", "End");
            for (Event event : events) {
                System.out.printf("%n  [%02d]    %-16s    %-10s    %-14s    %-14s ", event.getID(), event.getTitle(), event.getLocation().getName(), event.getStart().format(CUSTOMFORMAT), EventHelperService.getEndOfEvent(event).format(CUSTOMFORMAT));
            }
            System.out.println("\n");
        }

    }

    private Location locationSelector(Location[] locations){
        for (int i = 0; i < locations.length; i++) {
            System.out.printf("%d: %s%n", i+1, locations[i].getName());
        }
        int locationPlace = VALIDATIONSERVICE.getValidIntInRangeFromUser("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }

    private Location[] getFreeLocationsOnDate(LocalDateTime startDate, int length, Unit unit) {
        Location[] freeLocations = new Location[0];
        for (Location location : locations) {
            if (EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, unit).length == 0) {
                freeLocations = ArrayHelper.add(freeLocations, location);
            }
        }
        return freeLocations;
    }

}
