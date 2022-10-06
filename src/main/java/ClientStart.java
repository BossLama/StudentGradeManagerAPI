import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientStart {

    public static void main(String[] args) throws IOException, InterruptedException {
        //createStudent("Jonas", "Riemer", "12Q4");
        createGrade("3", "schulaufgabe", "deutsch", 2.5, 1);
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

    public static void createStudent(String firstname, String lastname, String classtag){
       try{
           HttpClient client = HttpClient.newHttpClient();
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create("http://localhost:8974/api/student"))
                   .POST(HttpRequest.BodyPublishers.ofString("{" +
                           "\"action\":\"create\"," +
                           "\"data\": {" +
                           "    \"firstname\": \"" + firstname + "\"," +
                           "    \"lastname\": \"" + lastname + "\"," +
                           "    \"classtag\": \"" + classtag + "\"," +
                           "  }" +
                           "}"))
                   .build();

           HttpResponse<String> response = client.send(request,
                   HttpResponse.BodyHandlers.ofString());

           System.out.println(response.body());
       }catch (Exception e){
           System.out.println("Fehler -> " + e.getMessage());
       }
    }

    public static void createGrade(String student_id, String gradetype, String subject, Double value, Integer weight){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/grade"))
                    .POST(HttpRequest.BodyPublishers.ofString("{" +
                            "\"action\":\"create\"," +
                            "\"data\": {" +
                            "    \"student-id\": \"" + student_id + "\"," +
                            "    \"grade-type\": \"" + gradetype + "\"," +
                            "    \"subject\": \"" + subject + "\"," +
                            "    \"value\": \"" + value + "\"," +
                            "    \"weight\":\"" + weight + "\"" +
                            "  }" +
                            "}"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.body());
        }catch (Exception e){
            System.out.println("Fehler -> " + e.getMessage());
        }
    }

    public static void updateStudent(String student_id, String firstname, String lastname, String classtag){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8974/api/student"))
                    .POST(HttpRequest.BodyPublishers.ofString("{" +
                            "\"action\":\"update\"," +
                            "\"student-id\":\"" + student_id + "\"," +
                            "\"data\": {" +
                            "    \"firstname\": \"" + firstname + "\"," +
                            "    \"lastname\": \"" + lastname + "\"," +
                            "    \"classtag\": \"" + classtag + "\"," +
                            " }" +
                            "}"))
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
