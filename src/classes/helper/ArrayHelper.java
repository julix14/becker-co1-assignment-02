package classes.helper;

import classes.Location;

import java.util.Arrays;

public class ArrayHelper {
    /**
     * Adds an element to the end of an array
     *
     * @param array  -- the array to add to
     * @param object -- the object to add
     * @return -- the new array
     * @throws NullPointerException -- if the array is null
     */
    public static <T> T[] add(T[] array, T object) {
        // Create a copy of the array with an extra space
        T[] newArray = copyAndGrowBy1(array);

        // Add an object to the end of the array
        newArray[newArray.length - 1] = object;
        return newArray;
    }

    /**
     * Adds a new, free slot to the end of an array
     *
     * @param array -- the array to add the free slot to
     * @return -- the new, bigger array
     * @throws NullPointerException -- if the array is null
     */
    public static <T> T[] copyAndGrowBy1(T[] array) {
        // Return an array with the same content as the input array, but with an additional empty element at the end
        return Arrays.copyOf(array, array.length + 1);
    }

    /**
     * removes a location Object from an array
     *
     * @param array -- the location array
     * @return -- the new, smaller array
     * @throws NullPointerException -- if the array is null
     */
    public static Location[] remove(Location[] array, Location object) {
        // Remove the given object from the array
        Location[] newArray = new Location[0];

        // Go through the array and add all elements that are not equal to the given object to the new array
        for (Location item : array) {
            if (!item.equals(object)) {
                newArray = ArrayHelper.add(newArray, item);
            }
        }
        return newArray;
    }
}
