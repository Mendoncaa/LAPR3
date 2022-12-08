package app.ui.console;

import app.controller.BasketListController;
import app.ui.console.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;

public class BasketListUI implements Runnable {

    private BasketListController controller = new BasketListController();

    @Override
    public void run() {
        System.out.println("\n###        Basket list        ###");
        System.out.println();

        String path = Utils.readLineFromConsole("\nFile path to read: ");
        if (path != null) {
            if (this.controller.importBasketList(new File(path))){
                System.out.println("Basket list imported successfully!");
            } else {
                System.out.println("Error importing basket list!");
            }
        }else {
            System.out.println("Invalid path!");
        }
    }
}

