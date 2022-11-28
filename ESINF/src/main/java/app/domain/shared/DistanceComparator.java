package app.domain.shared;

import app.domain.model.ClientsProducers;
import app.domain.model.HubAndDist;

import java.util.Comparator;

public class DistanceComparator implements Comparator<HubAndDist> {


    @Override
    public int compare(HubAndDist o1, HubAndDist o2) {
        return Integer.compare(o1.getDist(), o2.getDist());
    }
}
