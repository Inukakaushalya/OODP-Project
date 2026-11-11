package com.urbantech.smartcity360.patterns.chain;

/** Abstract handler: keeps a link to the next handler and defines handle(). */
public abstract class ComplaintHandler {

    private ComplaintHandler handler;

    public ComplaintHandler getHandler() {
        return handler;
    }

    public void setHandler(ComplaintHandler handler) {
        this.handler = handler;
    }

    public abstract void handle(Complaint complaint);
}
