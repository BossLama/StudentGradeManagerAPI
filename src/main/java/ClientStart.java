import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientStart {

    public static void main(String[] args) throws IOException, InterruptedException {
        createGrade();
    }


    public static void getClasslist(String classlist){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/student?list=true&classtag=" + classlist))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
    }

    public static void createGrade(){
       try{
           HttpClient client = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8974/api/grade"))
                   .POST(HttpRequest.BodyPublishers.ofString("{\"hello\":\"world\"}"))
                   .build();

           HttpResponse<String> response = client.send(request,
                   HttpResponse.BodyHandlers.ofString());

           System.out.println(response.body());
       }catch (Exception e){
           System.out.println("Fehler -> " + e.getMessage());
       }
    }

    public static void getClasslistSorted(String classlist, String sorted){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/student?list=true&classtag=" + classlist + "&sort-by=" + sorted))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
    }


}
