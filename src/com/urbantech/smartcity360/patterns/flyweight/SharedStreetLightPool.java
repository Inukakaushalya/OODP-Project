package com.urbantech.smartcity360.patterns.flyweight;

import java.util.ArrayList;

/** Flyweight factory: returns an existing shared object, or creates one only the first time. */
public class SharedStreetLightPool {

    private static final ArrayList<SharedStreetLight> sharedList = new ArrayList<>();

    public static SharedStreetLight getSharedStreetLight(String type, int wattage, String colour) {
        SharedStreetLight found = null;
        for (SharedStreetLight item : sharedList) {
            if (type.equals(item.getType()) && wattage == item.getWattage() && colour.equals(item.getColour())) {
                found = item;
                break;
            }
        }
        if (found == null) {
            found = new SharedStreetLight(type, wattage, colour);
            sharedList.add(found);
        }
        return found;
    }

    public static int getPoolSize() {
        return sharedList.size();
    }

    public static void clear() {
        sharedList.clear();
    }
}
