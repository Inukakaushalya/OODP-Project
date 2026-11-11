package com.urbantech.smartcity360.patterns.flyweight;

/**
 * Context object: holds the EXTRINSIC state (unique per light): id, location and status,
 * plus a reference to the shared intrinsic data.
 */
public class StreetLight extends AbstractStreetLight {

    private final int id;
    private final String location;
    private boolean on;
    private final SharedStreetLight sharedStreetLight;

    public StreetLight(int id, String type, int wattage, String colour, String location, boolean on) {
        this.id = id;
        this.location = location;
        this.on = on;
        this.sharedStreetLight = SharedStreetLightPool.getSharedStreetLight(type, wattage, colour);
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public SharedStreetLight getSharedStreetLight() {
        return sharedStreetLight;
    }

    @Override
    public String describe() {
        return "Light " + id + " | " + sharedStreetLight.getType() + " " + sharedStreetLight.getWattage()
                + "W " + sharedStreetLight.getColour() + " | " + location + " | " + (on ? "ON" : "OFF");
    }
}
