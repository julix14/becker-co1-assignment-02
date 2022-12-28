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
    private final ValidationService validationService = new ValidationService();

    private final EventPlanner eventPlanner;

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
                case 2 -> eventPlanner.showAllEvents();
                case 3 -> eventPlanner.showEventsByTitle();
                case 4 -> eventPlanner.showEventsByLocation();
                case 5 -> eventPlanner.showEventsByParticularDate();
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
