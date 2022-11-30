package app.domain.shared;

import app.domain.model.Path;

import java.util.Comparator;

public class AverageComparator implements Comparator<Path> {


    @Override
    public int compare(Path o1, Path o2) {
        if (o1.getAverageDist() > o2.getAverageDist()) {
            return 1;
        } else if (o1.getAverageDist() < o2.getAverageDist()) {
            return -1;
        } else {
            return 0;
        }
    }
}
