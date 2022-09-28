
import api.APIServer;
import api.systemcheck.Systemchecker;
import database.DatabaseConnector;

public class Mainclass {

    public static void main(String[] args){
        DatabaseConnector.connect();
        APIServer.start();
        if(!Systemchecker.check()){
            System.out.println("System wird wieder heruntergefahren!");
            System.exit(0);
        }
    }
}
