package app.ui.console;



import app.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AgricManagerUI implements Runnable{
    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Create a watering dispositive ", new IrrigationDeviceCreationUI()));
        options.add(new MenuItem("Check current irrigation ", new IrrigationDeviceUI()));

        int option = 0;
        do
        {
            option = Utils.showAndSelectIndex(options, "\n\nAgricultural manager Menu:\n");

            if ( (option >= 0) && (option < options.size()))
            {
                options.get(option).run();
            }
        }
        while (option != -1 );
    }
}
