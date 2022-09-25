
import api.APIServer;
import database.DatabaseConnector;

public class Mainclass {

    public static void main(String[] args){
        DatabaseConnector.connect();
        APIServer.start();
    }
}
