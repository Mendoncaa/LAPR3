package app.ui;

import app.domain.shared.FilesReaderApp;
import app.ui.console.MainMenuUI;


public class Main {

    public static void main(String[] args) {

        FilesReaderApp.bootstrap();

        try {
            MainMenuUI menu = new MainMenuUI();

            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
