package helper;

import classes.Location;
import classes.event.Event;

import java.util.Arrays;
import java.util.Comparator;

public class ArrayHelper {
    public static Event[] add(Event[] array, Event object){
        Event[] newArray = copyAndGrowBy1(array);
        newArray[newArray.length - 1] = object;
        return newArray;
    }

    public static Location[] add(Location[] array, Location object){
        Location[] newArray = copyAndGrowBy1(array);
        newArray[newArray.length - 1] = object;
        return newArray;
    }
    
    public static Event[] copyAndGrowBy1(Event[] array){
        return Arrays.copyOf(array, array.length + 1);
    }

    public static Location[] copyAndGrowBy1(Location[] array) {
        return Arrays.copyOf(array, array.length + 1);
    }

    public static Event[] sortById(Event[] array) {
        Event[] sortedArray = Arrays.copyOf(array, array.length);
        Arrays.sort(sortedArray, Comparator.comparingInt(Event::getID));
        return sortedArray;
    }

    public static Location[] remove(Location[] array, Location object) {
        Location[] newArray = new Location[array.length - 1];
        int index = 0;
        for (Location location : array) {
            if (!location.equals(object)) {
                newArray[index] = location;
                index++;
            }
        }
        return newArray;
    }
}
