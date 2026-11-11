package com.urbantech.smartcity360.patterns.decorator;

/**
 * Component interface of the Decorator pattern (Part A).
 * Every data stream - plain or wrapped with extra layers - offers the same methods.
 */
public interface DataStream {

    String getDescription();

    String getData();

    String getLog();
}
