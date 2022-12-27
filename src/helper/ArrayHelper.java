package helper;

import classes.Location;
import classes.event.Event;

import java.util.Arrays;

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

    public static Event[] addAll(Event[] array, Event[] objects){
        Event[] newArray = copyAndGrowByN(array, objects.length);
        System.arraycopy(objects, 0, newArray, array.length, objects.length);
        return newArray;
    }

    private static Event[] copyAndGrowByN(Event[] array, int length) {
        return Arrays.copyOf(array, array.length + length);
    }

    public static Event[] copyAndGrowBy1(Event[] array){
        return Arrays.copyOf(array, array.length + 1);
    }

    public static Location[] copyAndGrowBy1(Location[] array){
        return Arrays.copyOf(array, array.length + 1);
    }
}
