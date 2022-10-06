
import api.APIServer;
import api.configuration.ConfigHandler;
import api.systemcheck.Systemchecker;
import database.DatabaseConnector;

import java.util.Scanner;

public class Mainclass {


    public static void main(String[] args){
        Systemchecker.loadDatabaseConfig();
        Systemchecker.loadServerConfig();
        DatabaseConnector.connect();
        APIServer.start();
        if(!Systemchecker.check()) {
            System.out.println("System wird wieder heruntergefahren!");
            System.exit(0);
        }
    }

}
