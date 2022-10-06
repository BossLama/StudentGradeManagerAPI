
import api.APIServer;
import api.systemcheck.Systemchecker;
import database.DatabaseConnector;

import java.util.Scanner;

public class Mainclass {

    private static final String PASSWORD = "GradeAPI2022";

    public static void main(String[] args){

        System.out.print("Gebe das Passwort ein: ");
        Scanner password = new Scanner(System.in);
        String insert;
        while((insert = password.nextLine()) == null){

        }
        if(insert.equals(PASSWORD)){
            System.out.println("Login successfully");
            DatabaseConnector.connect();
            APIServer.start();
            if(!Systemchecker.check()){
                System.out.println("System wird wieder heruntergefahren!");
                System.exit(0);
            }
        }else{
            System.out.println("Password incorrect");
        }
    }
}
