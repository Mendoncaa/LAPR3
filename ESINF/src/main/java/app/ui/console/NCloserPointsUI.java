package app.ui.console;


import app.controller.App;
import app.controller.CloserPointsController;
import app.domain.model.ClientBasket;
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
                System.out.println("\n----------Defining Hubs----------\n\nHow much do you want? ");
                n = scanner.nextInt();
                System.out.println();
                if (n > 0) {
                    exit = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid Input");
            }
        } while (!exit);

        Set<Path> closerPoints = ctrl.getCloserPoints(App.getInstance().getCompany().getClientsProducersGraph());
        Set<Path> closerPoints2 = closerPoints;

        Iterator<Path> iterator = closerPoints.iterator();
        Iterator<Path> iterator2 = closerPoints2.iterator();

        for (int i = 0; i < n; i++) {
            if (iterator2.hasNext()) {
                App.getInstance().getCompany().getHubStore().addHub(iterator.next().getEntity());
                Path actual = iterator2.next();
                System.out.println(i + 1 + ". " + actual.getEntity() + "    Average :  " + actual.getAverageDist() + " meters");
            }
        }
    }
}
