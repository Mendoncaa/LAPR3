package app.ui;

import app.domain.shared.FilesReaderApp;
import app.ui.console.MainMenuUI;

import java.io.File;


public class Main {

    public static void main(String[] args)
    {

        File graphVertexFile = new File("./ESINF/src/files/Small/clientes-produtores_small.csv");
        File graphEdgeFile = new File("./ESINF/src/files/Small/distancias_small.csv");
        FilesReaderApp.readProducerCSV(graphVertexFile, graphEdgeFile);

        try
        {
            MainMenuUI menu = new MainMenuUI();

            menu.run();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
