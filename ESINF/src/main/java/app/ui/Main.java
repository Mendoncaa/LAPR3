package app.ui;

import app.domain.shared.FilesReaderApp;
import app.ui.console.MainMenuUI;

import java.io.File;


public class Main {

    public static void main(String[] args)
    {
        try
        {
            MainMenuUI menu = new MainMenuUI();

            File graphVertexFile = new File("files/Small/clientes-produtores_small.csv");
            File graphEdgeFile = new File("files\\Small\\distancias_small.csv");
            FilesReaderApp.readProducerCSV(graphVertexFile, graphEdgeFile);

                menu.run();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
