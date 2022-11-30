package app.ui.console;



import app.ui.console.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class RegisterUI implements Runnable {
    @Override
    public void run() {

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem(" An agricultural manager ", new AgricManagerRegisterUI()));
        options.add(new MenuItem(" A distributional manager ", new DistManagerRegisterUI()));
        options.add(new MenuItem(" A client ", new ClientRegisterUI()));
        options.add(new MenuItem(" A driver ", new DriverRegisterUI()));


        int option;
        do
        {
            option = Utils.showAndSelectIndex(options, "\n\nRegister:");

            if ( (option >= 0) && (option < options.size()))
            {
                options.get(option).run();
            }
        }
        while (option != -1 );
    }
}
