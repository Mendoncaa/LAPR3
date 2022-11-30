package app.ui.console;


import app.controller.App;
import app.controller.CloserPointsController;
import app.domain.model.Path;
import app.domain.shared.AverageComparator;


import java.util.*;


public class NCloserPointsUI implements Runnable {

    private CloserPointsController ctrl = new CloserPointsController();

    @Override
    public void run() {

        boolean exit = false;
        int n = 0;
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println("\nTop N Closer Points\nWrite the N :");
                n = scanner.nextInt();
                if (n > 0) {
                    exit = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        } while (!exit);

        ArrayList<Path> closerPoints = ctrl.getCloserPoints(App.getInstance().getCompany().getClientsProducersGraph());
        Set<Path> closerPointsSet = new TreeSet<>(new AverageComparator());

        closerPointsSet.addAll(closerPoints);

        Iterator<Path> iterator = closerPointsSet.iterator();

        if (closerPointsSet.size()>=n) {
            System.out.println("\nTop " + n + " Closer Points :\n");
            for (int i = 0; i < n; i++) {
                Path path = iterator.next();
                System.out.println(i + 1 + ". " + path.getEntity() + "    Average :  " + path.getAverageDist() + " meters");
            }
        }else {
            System.out.println("\nTop " + closerPointsSet.size() + " Closer Points :\n");
            for (int i = 0; i < closerPoints.size(); i++) {
                Path path = iterator.next();
                System.out.println(i + 1 + ". " + path.getEntity() + "    Average :  " + path.getAverageDist() + " meters");
            }
        }
    }
}
