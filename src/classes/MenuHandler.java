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
    };
    ValidationService validationService = new ValidationService();

    EventPlanner eventPlanner;

    public MenuHandler(EventPlanner eventPlanner) {
        this.eventPlanner = eventPlanner;
    }

    public void runMenu() {
        int selected = 0;
        while (selected != 8){
            printMenu();
            selected = selectMenuItem();
            switch (selected){
                case 1 -> eventPlanner.createNewEvent();
                case 2 -> eventPlanner.printAllEvents();
                case 3 -> eventPlanner.getEventsByTitle();
                case 4 -> eventPlanner.getEventsByLocation();
                case 5 -> eventPlanner.getEventsByDate();
                case 6 -> eventPlanner.showMostUsedLocation();
                case 7 -> eventPlanner.showLeastUsedLocation();
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
        return validationService.validateInputIsInRange("Please select a menu item: ", 1, MENUITEMS.length);
    }
}
