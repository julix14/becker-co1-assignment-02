import helper.validation.ValidationService;

public class Starter {
    public static void main(String[] args) {
        ValidationService validationService = new ValidationService();

        System.out.println("Hello on the event planner!");
        int locationCount = validationService.validateInputIsInt("For how many locations do you want to plan events? ");

        new EventPlanner(locationCount).runPlanner();
    }


}