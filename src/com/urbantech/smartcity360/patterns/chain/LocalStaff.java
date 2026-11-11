package com.urbantech.smartcity360.patterns.chain;

/** First handler in the chain: severity 1 - 3. */
public class LocalStaff extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        if (complaint.getSeverity() <= 3) {
            complaint.addTrace("Local staff: severity " + complaint.getSeverity()
                    + " -> handled the '" + complaint.getCategory() + "' complaint");
            complaint.setHandledBy("Local staff");
        } else {
            complaint.addTrace("Local staff: severity " + complaint.getSeverity()
                    + " -> cannot handle, escalating to Supervisor");
            this.getHandler().handle(complaint);
        }
    }
}
