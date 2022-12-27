package helper.validation;

import helper.input.UserInputService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public double validateInputIsDouble(String message) {
        String input;
        int tries = 0;
        do{
            //Get a String Input from the user
            input = userInputService.getStringFromUserWithMessage(message);

            //To assert the input is an Integer, try to parse it to an Integer
            //If so, return the Integer
            //If not, print an error message and ask for input again
            try {
                return Double.parseDouble(input);
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

    public LocalDateTime validateInputIsLocalDateTime(String message){
        String input;
        int tries = 0;
        do{
            //Get a String Input from the user
            input = userInputService.getStringFromUserWithMessage(message);

            //To assert the input is an Integer, try to parse it to an Integer
            //If so, return the Integer
            //If not, print an error message and ask for input again
            try {
                return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Input is not a date");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return null;
    }

    public LocalDate validateInputIsLocalDate(String message){
        String input;
        int tries = 0;
        do{
            //Get a String Input from the user
            input = userInputService.getStringFromUserWithMessage(message);

            //To assert the input is an Integer, try to parse it to an Integer
            //If so, return the Integer
            //If not, print an error message and ask for input again
            try {
                return LocalDate.parse(input, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Input is not a date");
            }
            tries++;
        }while (tries < 3);
        System.out.println("You have entered an invalid input 3 times. Exiting the program");
        System.exit(1);
        return null;
    }

}
