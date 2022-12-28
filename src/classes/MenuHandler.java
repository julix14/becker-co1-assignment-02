package classes;

import helper.validation.ValidationService;

public class MenuHandler {
    private final String[] MENUITEMS = {
            "Create new event",
            "Show a report of all events",
            "Search events by title",
            "Search events by location",
            "Show all events on particular date",
            "Show the most used location",
            "Show the least used location",
            "Exit"
    };
    private final ValidationService VALIDATION_SERVICE;
    private final EventPlanner EVENT_PLANNER;

    public MenuHandler(EventPlanner eventPlanner) {
        this.EVENT_PLANNER = eventPlanner;
        this.VALIDATION_SERVICE = new ValidationService();
    }

    public void runMenu() {
        int selected = 0;
        while (selected != 8) {
            printMenu();
            selected = selectMenuItem();
            switch (selected) {
                case 1 -> this.EVENT_PLANNER.createNewEvent();
                case 2 -> this.EVENT_PLANNER.showAllEvents();
                case 3 -> this.EVENT_PLANNER.showEventsByTitle();
                case 4 -> this.EVENT_PLANNER.showEventsByLocation();
                case 5 -> this.EVENT_PLANNER.showEventsByParticularDate();
                case 6 -> this.EVENT_PLANNER.showMostUsedLocation();
                case 7 -> this.EVENT_PLANNER.showLeastUsedLocation();
            }
        }
        System.out.println("Thank you for using the event planner!");
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void printMenu() {
        for (int i = 0; i < MENUITEMS.length; i++) {
            System.out.printf("[%d] - %s%n", i + 1, MENUITEMS[i]);
        }
    }

    private int selectMenuItem(){
        return VALIDATION_SERVICE.getValidIntInRangeFromUser("Please select a menu item: ", 1, MENUITEMS.length);
    }
}
