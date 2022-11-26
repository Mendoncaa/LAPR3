package app.ui;

import app.domain.shared.FilesReaderApp;
import app.ui.console.MainMenuUI;

import java.io.File;


public class Main {

    public static void main(String[] args)
    {

        File graphVertexFile = new File("D:/Pastas/ISEP/LAPR3 2022/sem3pi2022_23_g063/ESINF/src/files/Small/clientes-produtores_small.csv");
        File graphEdgeFile = new File("src/files/Small/distancias_small.csv");
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
