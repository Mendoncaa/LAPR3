package app.ui.console;

import app.domain.model.ClientsProducers;
import app.domain.shared.ClosestPointsCheck;

public class DebugUI implements Runnable {

    private ClientsProducers hub = new ClientsProducers("CT5", 39.823f, -7.4931f,"E3");

    @Override
    public void run() {
        ClosestPointsCheck.closestProducers(hub,2);
    }
}
