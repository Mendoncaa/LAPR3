package app.ui.console;




import app.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ClientUI implements Runnable{
    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        //options.add(new MenuItem("Register ", new RegisterUI()));
        //options.add(new MenuItem("Get information ", new GetInfoUI()));
        options.add(new MenuItem("Get shortest path between Clients", new GraphUI()));
        options.add(new MenuItem("Get closest hub", new ClosestHubUI()));


        int option = 0;
        do
        {
            option = Utils.showAndSelectIndex(options, "\n\nClient Menu:\n");

            if ( (option >= 0) && (option < options.size()))
            {
                options.get(option).run();
            }
        }
        while (option != -1 );
    }
}
