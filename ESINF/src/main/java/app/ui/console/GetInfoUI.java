package app.ui.console;


import app.ui.console.utils.Utils;

import java.util.NoSuchElementException;

public class GetInfoUI implements Runnable {
    @Override
    public void run() {
        int option;

        do {

            option = ChooseOption();

            if (option!=0) {
                GetOptionInfoUI menu = new GetOptionInfoUI(option);
                menu.run();
            }

        }while (option!=0);
    }

    private static int ChooseOption(){

        String[] roles = {" The agricultural managers "," The distribution managers "," The clients "," The drivers ", " All users"};
        System.out.printf("%n%n%s%n%n","Choose an option :");

        int option = 1;

        for (int i = 0; i< roles.length;i++){
            System.out.println(" "+option+". "+roles[i]);
            option++;
        }

        System.out.print("\n0 - Cancel");

        System.out.println();
        int choose = 0;

        try {
            choose = Utils.readIntegerFromConsole("Type here : ");
        }
        catch (NoSuchElementException | NumberFormatException e ){
            System.out.println("Option chose doesn't exist.");
        }
        if(choose<0 || choose>5){
            System.out.println("Option chose doesn't exist.");
        }
        return choose;
    }
}
