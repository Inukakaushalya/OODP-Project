package com.urbantech.smartcity360.patterns.chain;

/** Last handler in the chain: severity 8 - 10. It has no next handler. */
public class CityManager extends ComplaintHandler {

    @Override
    public void handle(Complaint complaint) {
        if (complaint.getSeverity() >= 8) {
            complaint.addTrace("City manager: severity " + complaint.getSeverity()
                    + " -> handled the '" + complaint.getCategory() + "' complaint");
            complaint.setHandledBy("City manager");
        } else {
            complaint.addTrace("City manager: cannot handle this complaint");
        }
    }
}
