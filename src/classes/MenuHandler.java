package classes;

import helper.UserInputService;

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
    private final EventPlanner EVENT_PLANNER;

    public MenuHandler(EventPlanner eventPlanner) {
        this.EVENT_PLANNER = eventPlanner;
    }

    public void runMenu() {
        int selected;
        do {
            // Print the menu
            printMenu();

            // Let the user select an option
            selected = selectMenuItem();

            // Execute the selected option
            switch (selected) {
                case 1 -> this.EVENT_PLANNER.createNewEvent();
                case 2 -> this.EVENT_PLANNER.showAllEvents();
                case 3 -> this.EVENT_PLANNER.showEventsByTitle();
                case 4 -> this.EVENT_PLANNER.showEventsByLocation();
                case 5 -> this.EVENT_PLANNER.showEventsByParticularDate();
                case 6 -> this.EVENT_PLANNER.showMostUsedLocation();
                case 7 -> this.EVENT_PLANNER.showLeastUsedLocation();
            }
            // If the user selected 8, the program will exit
        } while (selected != 8);
        System.out.println("Thank you for using the event planner!");
        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void printMenu() {
        // Print all MenuItems
        for (int i = 0; i < MENUITEMS.length; i++) {
            System.out.printf("[%02d] - %s%n", i + 1, MENUITEMS[i]);
        }
    }

    private int selectMenuItem(){
        return UserInputService.getValidIntInRangeFromUser("Please select a menu item: ", 1, MENUITEMS.length);
    }
}
