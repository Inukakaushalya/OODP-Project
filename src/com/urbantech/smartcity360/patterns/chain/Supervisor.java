package com.urbantech.smartcity360.patterns.chain;

/** Second handler in the chain: severity 4 - 7. */
public class Supervisor extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        if (complaint.getSeverity() <= 7) {
            complaint.addTrace("Supervisor: severity " + complaint.getSeverity()
                    + " -> handled the '" + complaint.getCategory() + "' complaint");
            complaint.setHandledBy("Supervisor");
        } else {
            complaint.addTrace("Supervisor: severity " + complaint.getSeverity()
                    + " -> cannot handle, escalating to City manager");
            this.getHandler().handle(complaint);
        }
    }
}
