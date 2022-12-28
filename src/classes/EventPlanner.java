package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.TimeUnit;
import helper.ArrayHelper;
import helper.EventHelperService;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventPlanner {
    private Location[] locations;
    private final UserInputService userInputService = new UserInputService();
    private final ValidationService validationService = new ValidationService();

    private int eventCount = 0;

    private Event[] events = new Event[0];

    public EventPlanner(int locationCount){
        //locations = setupLocations(locationCount + 1);
        locations = demoLocations();
    }

    private Location[] setupLocations(int locationCount){
        System.out.printf("%nPlease enter the name and max capacity of %d locations:", locationCount - 1);
        this.locations = new Location[locationCount];
        for (int i = 0; i < locationCount - 1; i++) {
            String name = userInputService.getStringFromUserWithMessage(String.format("%nPlease enter the name of location %d: ", i+1));
            int maxCapacity = validationService.validateInputIsInt(String.format("Please enter the max. capacity of %s: ", name));
            locations[i] = new Location(name, maxCapacity);
        }
        locations[locationCount - 1] = new Location("Online", -1);
        return locations;
    }

    private Location[] demoLocations(){
        Location[] locations = new Location[3];
        locations[0] = new Location("Online", -1);
        locations[1] = new Location("Room 1", 10);
        locations[2] = new Location("Room 2", 20);

        events = ArrayHelper.add(events, new OnlineEvent(1, "Online Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, TimeUnit.DAY , new String[]{"John", "Jane"}, locations[0]));
        events = ArrayHelper.add(events, new OnlineEvent(2, "Online Event 2", LocalDateTime.of(2021, 2, 1, 10, 0), 1, TimeUnit.DAY , new String[]{"John", "Jane"}, locations[0]));

        events = ArrayHelper.add(events, new OnsiteEvent(3, "Onsite Event 1", LocalDateTime.of(2021, 1, 1, 10, 0), 1, TimeUnit.DAY , new String[]{"John", "Jane"}, locations[1]));

        events = ArrayHelper.add(events, new OnsiteEvent(5, "Onsite Event 3", LocalDateTime.of(2021, 1, 1, 10, 0), 1, TimeUnit.HOUR , new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(6, "Onsite Event 4", LocalDateTime.of(2021, 2, 1, 10, 0), 1, TimeUnit.DAY , new String[]{"John", "Jane"}, locations[2]));
        events = ArrayHelper.add(events, new OnsiteEvent(7, "Onsite Event 5", LocalDateTime.of(2021, 3, 1, 10, 0), 1, TimeUnit.DAY , new String[]{"John", "Jane"}, locations[2]));

        return locations;
    }

    public void createNewEvent(){
        System.out.println("Create new event");
        // Get name of new Event
        String name = userInputService.getStringFromUserWithMessage("Please enter the name of the event: ");

        // Get Start of new Event
        LocalDateTime startDate = validationService.validateInputIsLocalDateTime("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");

        // Get length of new Event
        double length = validationService.validateInputIsDouble("Please enter the length of the event: ");

        // Get TimeUnit of new Event
        int timeUnitPlace = validationService.validateInputIsInRange("Please enter the time unit of the event: (1 = Hours, 2 = Days, 3 = Months)", 1, 3);
        TimeUnit timeUnit = TimeUnit.values()[timeUnitPlace-1];

        // Select Location of new Event
        System.out.println("Please select the location of the event:");
        Location location = locationSelector(locations);

        // Check if location is available
        // Disable Check if Online Event#
        boolean locationAvailable;
        if (location.getName().equals("Online")){
            locationAvailable = true;
        } else {
            locationAvailable = EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, timeUnit).length == 0;
        }

        while(!locationAvailable){
            Location[] freeLocations = getFreeLocationsOnDate(startDate, length, timeUnit);
            if (freeLocations.length != 0){
                System.out.println("The selected location is not available on the selected date. Please select another location:");
                location = locationSelector(freeLocations);
            } else {
                System.out.println("There are no free locations on the selected date. Please select another date:");
                startDate = validationService.validateInputIsLocalDateTime("Please enter the start date of the event: (DD.MM.YYYY HH:mm)");
            }
            if (location.getName().equals("Online")){
                locationAvailable = true;
            } else {
                locationAvailable = EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, timeUnit).length == 0;
            }
        }

        // Add participants
        int participantsCount;
        if (location.getName().equals("Online")){
            participantsCount = validationService.validateInputIsInt("Please enter the number of participants: ");
        } else {
            participantsCount = validationService.validateInputIsInRange("Please enter the number of participants: ", 1, location.getMaxCapacity());
        }

        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = userInputService.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i+1));
        }

        // Create Event and add to events-list
        Event event;
        if (location.getName().equals("Online")){
            event = new OnlineEvent(eventCount, name, startDate, length, timeUnit, participants, location);

        } else {
            event = new OnsiteEvent(eventCount, name, startDate, length, timeUnit, participants, location);
        }
        events = ArrayHelper.add(events, event);
        eventCount++;
    }
    public void printAllEvents(){
        printEvents(events);
        System.out.println();
    }

    public void getEventsByTitle(){
        String searchedName = userInputService.getStringFromUserWithMessage("Please enter the name of the searched event: ").toLowerCase();

        Event[] selectedEvents = new Event[0];
        for (Event event : events) {
            if (event.getTitle().toLowerCase().contains(searchedName)){
                selectedEvents = ArrayHelper.add(selectedEvents, event);
            }
        }
        printEvents(selectedEvents);
    }

    public void getEventsByLocation(){
        Location location = locationSelector(locations);
        printEvents(EventHelperService.eventsOnLocation(events, location));
    }

    public void getEventsByDate(){
        LocalDate searchedDate = validationService.validateInputIsLocalDate("Please enter the date of the searched events: (DD.MM.YYYY)");
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            allEvents = ArrayHelper.addAll(EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), searchedDate.atStartOfDay(), 1, TimeUnit.DAY), allEvents);
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
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        events = ArrayHelper.sortById(events);
        if (events.length == 0){
            System.out.println("No events found.");
        } else {
            System.out.printf("%d Events found:%n", events.length);
            System.out.print("  [ID] -- [Title] -- [Start] -- [End]");
            for (Event event : events) {
                System.out.printf("%n  [%d] -- %s -- %s -> %s ", event.getID(), event.getTitle(), event.getStart().format(customFormat), EventHelperService.getEndOfEvent(event).format(customFormat));
            }
            System.out.println("\n");
        }

    }

    private Location locationSelector(Location[] locations){
        for (int i = 0; i < locations.length; i++) {
            System.out.printf("%d: %s%n", i+1, locations[i].getName());
        }
        int locationPlace = validationService.validateInputIsInRange("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }

    private Location[] getFreeLocationsOnDate(LocalDateTime startDate, double length, TimeUnit timeUnit){
        Location[] freeLocations = new Location[0];
        for (Location location : locations){
            if (EventHelperService.eventsWhileDuration(EventHelperService.eventsOnLocation(events, location), startDate, length, timeUnit).length == 0){
                freeLocations = ArrayHelper.add(freeLocations, location);
            }
        }
        return freeLocations;
    }

}
