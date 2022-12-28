package helper;

import classes.Location;
import classes.event.Event;

import java.util.Arrays;

public class ArrayHelper {
    public static Event[] add(Event[] array, Event object){
        // Create a copy of the array with an extra space
        Event[] newArray = copyAndGrowBy1(array);

        // Add an object to the end of the array
        newArray[newArray.length - 1] = object;
        return newArray;
    }

    public static Location[] add(Location[] array, Location object) {
        // Create a copy of the array with an extra space
        Location[] newArray = copyAndGrowBy1(array);

        // Add an object to the end of the array
        newArray[newArray.length - 1] = object;
        return newArray;
    }

    public static Event[] copyAndGrowBy1(Event[] array){
        // Return an array with the same content as the input array, but with an additional empty element at the end
        return Arrays.copyOf(array, array.length + 1);
    }

    public static Location[] copyAndGrowBy1(Location[] array) {
        // Return an array with the same content as the input array, but with an additional empty element at the end
        return Arrays.copyOf(array, array.length + 1);
    }


    public static Location[] remove(Location[] array, Location object) {
        // Remove the given object from the array
        Location[] newArray = new Location[0];

        // Go through the array and add all elements that are not equal to the given object to the new array
        for (Location location : array) {
            if (!location.equals(object)) {
                ArrayHelper.add(newArray, location);
            }
        }
        return newArray;
    }
}
