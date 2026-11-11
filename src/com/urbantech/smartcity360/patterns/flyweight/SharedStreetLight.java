package com.urbantech.smartcity360.patterns.flyweight;

/**
 * Flyweight: holds the INTRINSIC state (same for many lights): type, wattage and colour.
 * One object of this class is shared by thousands of StreetLight objects.
 */
public class SharedStreetLight {

    private final String type;
    private final int wattage;
    private final String colour;

    public SharedStreetLight(String type, int wattage, String colour) {
        this.type = type;
        this.wattage = wattage;
        this.colour = colour;
    }

    public String getType() {
        return type;
    }

    public int getWattage() {
        return wattage;
    }

    public String getColour() {
        return colour;
    }
}
