package app.ui.console;

import app.controller.App;
import app.domain.shared.FilesReaderApp;
import app.ui.console.utils.Utils;

import java.io.File;

public class ImportClientsProducersUI implements Runnable {

    File graphVertexFileSmall = new File("ESINF/src/files/Small/clientes-produtores_small.csv");
    File graphEdgeFileSmall = new File("ESINF/src/files/Small/distancias_small.csv");
    File graphVertexFileBig = new File("ESINF/src/files/Big/clientes-produtores_big.csv");
    File graphEdgeFileBig = new File("ESINF/src/files/Big/distancias_big.csv");

    public void run() {

        //App.getInstance().getCompany().

        String og = Utils.readLineFromConsole("Do you wish to import Small or Big data files?\n");

        assert og != null;

        if(og.equalsIgnoreCase("Small")) {
            FilesReaderApp.readProducerCSV(graphVertexFileSmall,graphEdgeFileSmall );
        }else if (og.equalsIgnoreCase("Big")) {
            FilesReaderApp.readProducerCSV(graphVertexFileBig,graphEdgeFileBig );
        }else{
            System.out.println("Invalid Input.\n");
        }
    }


}
