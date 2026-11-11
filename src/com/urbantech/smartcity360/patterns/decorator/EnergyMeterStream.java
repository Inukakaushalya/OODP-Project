package com.urbantech.smartcity360.patterns.decorator;

import java.nio.charset.StandardCharsets;

/**
 * Concrete component: the plain data stream coming from an energy meter.
 * It knows nothing about encryption, authentication or compression.
 */
public class EnergyMeterStream implements DataStream {

    private final String meterId;
    private final double reading;

    public EnergyMeterStream(String meterId, double reading) {
        this.meterId = meterId;
        this.reading = reading;
    }

    @Override
    public String getDescription() {
        return "Energy meter stream";
    }

    @Override
    public String getData() {
        return "meter=" + meterId + ";reading=" + reading + "kWh;unit=kWh;status=OK;"
                + "city=Colombo;zone=Western;meter=" + meterId + ";unit=kWh;status=OK";
    }

    @Override
    public String getLog() {
        int bytes = getData().getBytes(StandardCharsets.UTF_8).length;
        return "[Original] " + bytes + " bytes: " + getData();
    }
}
