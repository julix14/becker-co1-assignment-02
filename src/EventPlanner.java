import classes.Location;
import classes.event.Event;

public class EventPlanner {
    private Location[] locations;
    private final String[] FUNCTIONS = {
            "Create new event",
            "Show a report of all events",
            "Search events by title",
            "Search events by location",
            "Show all events on particular date",
            "Show the most used location",
            "Show the least used location",
    };

    public EventPlanner(int locationCount){
        locations = new Location[locationCount];
    }

    public void runPlanner(){
        //While loop with MenuHandlerRun Call and switch case with return value
    }

    private void createNewEvent(){
        //TODO: implement this method
    }

    private Event[] getAllEvents(){
        return null;
    }

    private Event[] getEventsByName(){
        return null;
    }

    private Event[] getEventsByLocation(){
        return null;
    }

    private Event[] getEventsByDate(){
        return null;
    }

    private void showMostUsedLocation(){

    }

    private void showLeastUsedLocation(){

    }

    private void printEvents(Event[] events){

    }

}
