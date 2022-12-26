package helper.validation;

import helper.input.UserInputService;

public class ValidationService {
    private final UserInputService userInputService = new UserInputService();

    public int validateInputIsInt(String message) {
        String input;
        int tries = 0;
        do{
            //Get a String Input from the user
            input = userInputService.getStringFromUserWithMessage(message);

            //To assert the input is an Integer, try to parse it to an Integer
            //If so, return the Integer
            //If not, print an error message and ask for input again
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input is not a number");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return -1;
    }

    public int validateInputIsInRange(String message, int min, int max) {
        String input;
        int tries = 0;
        do{
            //Get a String Input from the user
            input = userInputService.getStringFromUserWithMessage(message);
            String regexString = "[" + min + "-" + max + "]";
            //Check if the input is a number and in the range
            if (!input.matches("^[0-9]+$")) {
                System.out.println("Input is not a number, please enter a number between " + min + " and " + max);
            } else if (!input.matches(regexString)) {
                System.out.println("Input is not in range, please enter a number between " + min + " and " + max);
            } else {
                return Integer.parseInt(input);
            }
           tries++;
        }while (tries < 3);

        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return -1;
    }

}
