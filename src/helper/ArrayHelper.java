package helper;

import classes.event.Event;

import java.util.Arrays;

public class ArrayHelper {
    public static Event[] add(Event[] array, Event object){
        Event[] newArray = copyAndGrowBy1(array);
        newArray[newArray.length - 1] = object;
        return newArray;
    }

    public static Event[] copyAndGrowBy1(Event[] array){
        Event[] newArray = new Event[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
}
