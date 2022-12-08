package app.ui.console;

import app.controller.App;
import app.ui.console.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainMenuUI {

    public MainMenuUI()
    {
    }

    public void run() throws IOException
    {
        List<MenuItem> options = new ArrayList<MenuItem>();
        //options.add(new MenuItem("Do Login", new AuthUI()));
        options.add(new MenuItem("Get shortest path between Clients", new GraphUI()));
        options.add(new MenuItem("Get smallest fully connected network", new GraphSmallestUI()));
        options.add(new MenuItem("Define the hubs of the distribution network",new NCloserPointsUI()));
        options.add(new MenuItem("Get closest hub", new ClosestHubUI()));
        options.add(new MenuItem("Register an irrigation device ", new IrrigationDeviceCreationUI()));
        options.add(new MenuItem("Check current irrigation ", new IrrigationDeviceUI()));

        int option = 0;
        do
        {
            option = Utils.showAndSelectIndex(options, "\n\nMain Menu\n");

            if ( (option >= 0) && (option < options.size()))
            {
                options.get(option).run();
            }
        }
        while (option != -1 );

        for (int i = 0; i < App.getInstance().getCompany().getHubStore().size(); i++) {
            System.out.println(App.getInstance().getCompany().getHubStore().getHub(i));
        }
    }


}
