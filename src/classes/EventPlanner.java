package classes;

import classes.event.Event;
import classes.event.OnlineEvent;
import classes.event.OnsiteEvent;
import classes.event.TimeUnit;
import helper.ArrayHelper;
import helper.input.UserInputService;
import helper.validation.ValidationService;

import java.time.LocalDateTime;

public class EventPlanner {
    private Location[] locations;
    UserInputService userInputService = new UserInputService();
    ValidationService validationService = new ValidationService();

    int eventCount = 0;

    public EventPlanner(int locationCount){
        locations = setupLocations(locationCount + 1);
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
        Location location = locationSelector();

        // Check if location is available
        // Disable Check if Online Event#
        boolean locationAvailable;
        if (location.getName().equals("Online")){
            locationAvailable = true;
        } else {
            locationAvailable = location.eventsWhileDuration(startDate, length, timeUnit).length == 0;
        }

        while(!locationAvailable){
            System.out.println("The location is not available at this time. Please select another location:");
            location = locationSelector();
            locationAvailable = location.eventsWhileDuration(startDate, length, timeUnit).length == 0;
        }

        // Add participants
        int participantsCount = validationService.validateInputIsInRange("Please enter the number of participants: ", 1, location.getMaxCapacity());
        String[] participants = new String[participantsCount];
        for (int i = 0; i < participantsCount; i++) {
            participants[i] = userInputService.getStringFromUserWithMessage(String.format("Please enter the name of participant %d: ", i+1));
        }

        // Create Event and add to location
        if (location.getName().equals("Online")){
            Event event = new OnlineEvent(eventCount, name, startDate, length, timeUnit, participants, location);
            location.addEvent(event);

        } else {
            Event event = new OnsiteEvent(eventCount, name, startDate, length, timeUnit, participants, location);
            location.addEvent(event);

        }
    }
    public void printAllEvents(){
        Event[] allEvents = getAllEvents();
        printEvents(allEvents);
    }

    public void getEventsByTitle(){
        String searchedName = userInputService.getStringFromUserWithMessage("Please enter the name of the searched event: ").toLowerCase();
        Event[] allEvents = getAllEvents();
        Event[] selectedEvents = new Event[0];
        for (Event event : allEvents) {
            if (event.getTitle().toLowerCase().contains(searchedName)){
                ArrayHelper.add(selectedEvents, event);
            }
        }
        printEvents(selectedEvents);
    }

    public void getEventsByLocation(){
        Location location = locationSelector();
        printEvents(location.getEvents());
    }

    public void getEventsByDate(){
        LocalDateTime searchedDate = validationService.validateInputIsLocalDateTime("Please enter the date of the searched events: (DD.MM.YYYY HH:mm)");
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            ArrayHelper.addAll(location.eventsWhileDuration(searchedDate, 1, TimeUnit.DAY), allEvents);
        }
        printEvents(allEvents);
    }

    public void showMostUsedLocation(){
        Location mostUsedLocation = locations[0];
        for (Location location : locations) {
            if (location.getEvents().length > mostUsedLocation.getEvents().length){
                mostUsedLocation = location;
            }
        }
        System.out.printf("%nThe most used location is %s with %d events.%n", mostUsedLocation.getName(), mostUsedLocation.getEvents().length);
    }

    public void showLeastUsedLocation(){
        Location leastUsedLocation = locations[0];
        for (Location location : locations) {
            if (location.getEvents().length < leastUsedLocation.getEvents().length){
                leastUsedLocation = location;
            }
        }
        System.out.printf("%nThe least used location is %s with %d events.%n", leastUsedLocation.getName(), leastUsedLocation.getEvents().length);

    }

    private Event[] getAllEvents(){
        Event[] allEvents = new Event[0];
        for (Location location : locations) {
            if (location.getEvents().length > 0){
                ArrayHelper.addAll(location.getEvents(), allEvents);
            }
        }
        return allEvents;
    }

    private void printEvents(Event[] events){
        if (events.length == 0){
            System.out.println("No events found.");
        } else {
            for (Event event : events) {
                System.out.printf("- %s", event.getTitle());
            }
        }

    }

    private Location locationSelector(){
        for (int i = 0; i < locations.length; i++) {
            System.out.printf("%d: %s%n", i+1, locations[i].getName());
        }
        int locationPlace = validationService.validateInputIsInRange("Please enter the number of the location: ", 1, locations.length);
        return locations[locationPlace-1];
    }

}
