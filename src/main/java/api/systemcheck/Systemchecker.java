package api.systemcheck;

import database.DatabaseConnector;

import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;

public class Systemchecker {

    private static final double task_sum = 5.0;
    private static ArrayList<String> fails = new ArrayList<>();


    public static boolean check(){
        fails.clear();
        System.out.println();
        System.out.println("[i] ======= STARTET SYSTEMCHECK ======");
        System.out.println("[i] Prüft Endpoint Grade -> GET  (" + currentTaskSuccess(1) +"%)");
        checkGradeGet();
        System.out.println("[i] Prüft Endpoint Grade -> POST  (" + currentTaskSuccess(2) +"%)");
        checkGradePost();
        System.out.println("[i] Prüft Endpoint Student -> GET  (" + currentTaskSuccess(3) +"%)");
        checkStudentGet();
        System.out.println("[i] Prüft Endpoint Student -> POST  (" + currentTaskSuccess(4) +"%)");
        checkStudentPost();
        System.out.println("[i] Prüft Datenbankverbindung  (" + currentTaskSuccess(5) +"%)");
        checkSQLConnection();
        System.out.println();
        System.out.println("[i] Alle Systeme geprüft: Erkannte Fehler: " + fails.size());
        if(fails.size() > 0){
            for(int i = 0; i < fails.size(); i++){ System.out.println("[!] " + fails.get(i));}
            System.out.println("[i] ======= =============== ======");
            return false;
        }else{
            System.out.println("[i] System erfolgreich gestartet :)");
            System.out.println("[i] ======= =============== ======");
            return true;
        }


    }

    private static boolean checkGradeGet(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/grade?check"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.body().equalsIgnoreCase("positive - GET")) return true;
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
        fails.add("ERROR: Server kann GET-REQUEST für Endpoint 'api/grade/' nicht verarbeiten");
        return false;
    }

    private static boolean checkStudentGet(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/student?check"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.body().equalsIgnoreCase("positive - GET")) return true;
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
        fails.add("ERROR: Server kann GET-REQUEST für Endpoint 'api/student/' nicht verarbeiten");
        return false;
    }

    public static boolean checkGradePost(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/grade?check"))
                    .POST(HttpRequest.BodyPublishers.ofString("{\"hello\":\"world\"}"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.body().equalsIgnoreCase("positive - POST")) return true;
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
        fails.add("ERROR: Server kann POST-REQUEST für Endpoint 'api/grade/' nicht verarbeiten");
        return false;
    }

    public static boolean checkStudentPost(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/student?check"))
                    .POST(HttpRequest.BodyPublishers.ofString("{\"hello\":\"world\"}"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if(response.body().equalsIgnoreCase("positive - POST")) return true;
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
        fails.add("ERROR: Server kann POST-REQUEST für Endpoint 'api/student/' nicht verarbeiten");
        return false;
    }

    private static boolean checkSQLConnection(){
        try {
            if(DatabaseConnector.connection != null && DatabaseConnector.connection.isValid(3000)){
                return true;
            }else{
                fails.add("ERROR: Server hat keine Datenbankverbindung!");
                return false;

            }
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        fails.add("ERROR: Server hat keine Datenbankverbindung!");
        return false;
    }


    private static double currentTaskSuccess(int task){
        return (task / task_sum) * 100;
    }



}
