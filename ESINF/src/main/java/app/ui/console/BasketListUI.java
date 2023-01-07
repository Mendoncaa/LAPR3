package app.ui.console;

import app.controller.App;
import app.controller.BasketListController;
import app.ui.console.utils.Utils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;

public class BasketListUI implements Runnable {

    String path = "ESINF/src/files/Small/cabazes_small.csv";

    //String path = "ESINF/src/files/Big/cabazes_big.csv";
    private BasketListController controller = new BasketListController();

    @Override
    public void run() {
        System.out.println("\n###        Basket list        ###");
        App.getInstance().getCompany().getStock().getStock().clear();
        //String path = Utils.readLineFromConsole("\nFile path to read: ");
        if (path != null) {
            Pair<Integer, Integer> result = this.controller.importBasketList(new File(path));
            if (result != null) {
                System.out.println("\nBasket list imported successfully!");
                System.out.println("\nNumber of producers: " + result.getRight());
                System.out.println("Number of orders: " + result.getLeft());
                App.getInstance().getCompany().getStock().addHubs();
            } else {
                System.out.println("Error importing basket list!");
            }
        } else {
            System.out.println("Invalid path!");
        }
    }
}

