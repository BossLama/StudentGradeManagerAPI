package api.studentrequest;

import api.systemcheck.Systemchecker;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseConnector;
import elements.APIError;
import elements.Student;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Statement;
import java.util.Arrays;

public class StudentRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String request_method = exchange.getRequestMethod();
        String response = "";

        //KEY FOR SYSTEMCHECK
        if(exchange.getRequestURI().toString().equalsIgnoreCase("/api/student?check")){
            System.out.println("[i] Systemcheck erhalten - Students");
            response = "positive - " + exchange.getRequestMethod();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        //SAVE DATA AS STRING FROM REQUEST BODY
        String data = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        //EXECUTE DIFFERENT HANDLER (POST | GET)
        if(request_method.equalsIgnoreCase("post")){
            response = handlePost(data, exchange);
        }else if(request_method.equalsIgnoreCase("get")){
            response = handleGet(exchange.getRequestURI().toString(), exchange);
        }else response = APIError.UNKOWN_REQUEST_TYPE.toString();

        //SENDING RESPONSE TO CLIENT
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public String handlePost(String data, HttpExchange exchange){
        //WILL BE EXECUTED BY POST REQUESTS
        StringBuilder response = new StringBuilder("POST");
        try{
            //CREATE JSON OBJECT OF BODY-DATA
            JSONObject jsonObject = new JSONObject(data);
            //CHECK IS ACTION-KEY SET
            if(jsonObject.getString("action") == null || jsonObject.get("action") == "") return APIError.UNSET_ACTION_ERROR.toString();

            //EXECUTE DIFFERENT ACTIONS
            if(jsonObject.getString("action").equalsIgnoreCase("create")){
                //CREATE STUDENT
                //GET DATA VALUES
                JSONObject datas = jsonObject.getJSONObject("data");

                String firstname = datas.getString("firstname");
                String lastname = datas.getString("lastname");
                String classtag = datas.getString("classtag");

                Student student = new Student();
                student.setFirstname(firstname);
                student.setLastname(lastname);
                student.setClasstag(classtag);
                //CHECK IF DATA IS SAFE
                if(!student.isDataSet()) return APIError.WRONG_JSON_ERROR.toString();
                //SAVES TO DATABASE
                return DatabaseConnector.createStudent(student).toString();

            }else if(jsonObject.getString("action").equalsIgnoreCase("update")){
                //UPDATE STUDENT
                String studentid = jsonObject.getString("student-id");
                //GET DATA
                JSONObject datas = jsonObject.getJSONObject("data");
                int error = 0;
                if(datas.has("firstname")){
                    DatabaseConnector.updateStudent("firstname", datas.getString("firstname"), studentid);
                }
                if(datas.has("lastname")){
                    DatabaseConnector.updateStudent("lastname", datas.getString("lastname"), studentid);
                }
                if(datas.has("classtag")){
                    DatabaseConnector.updateStudent("classtag", datas.getString("classtag"), studentid);
                }
                return APIError.NO_ERROR.toString();

            }else if(jsonObject.getString("action").equalsIgnoreCase("delete")){
                //DELETE STUDENT BY ID
                String student_id = jsonObject.getString("student-id");
                return DatabaseConnector.deleteStudent(student_id).toString();
            }

        }catch (Exception e){
            //IF THERE IS AN ERROR --> RESPONSE ERROR
            response = new StringBuilder(APIError.WRONG_JSON_ERROR.toString());
        }
        return response.toString();
    }



    public String handleGet(String data, HttpExchange exchange){
        //WILL BE EXECUTED BY GET REQUESTS
        //SAVE DATA IN ARRAY
        String[] datapacks = data.split("\\?");
        if(datapacks.length == 2){
            datapacks = datapacks[1].split("&");
            if(datapacks.length == 1){
                //TRY TO EXECUTE SEARCH STUDENT BY ID
                String[] attribute = datapacks[0].split("=");
                if(attribute.length == 2){
                    //CHECK IS ATTRIBUTE NAME STUDENTID
                    if(attribute[0].equalsIgnoreCase("studentid")){
                        //GET STUDENT FROM DATABASE
                        Student student = DatabaseConnector.getStudentByID(attribute[1]);
                        //CHECK POSITIVE RESULT OR NOT
                        if(student.isDataSet()) return createResponse("student", new Gson().toJson(student));
                        return APIError.USER_NOT_FOUND_ERROR.toString();
                    }else return APIError.INVALID_GET_REQUEST_ERROR.toString();
                }else return APIError.INVALID_GET_REQUEST_ERROR.toString();
            }else if(datapacks.length == 2){
                String[] attribute1 = datapacks[0].split("=");
                String[] attribute2 = datapacks[1].split("=");
                //CHECK FOR LIST
                if(attribute1[0].equalsIgnoreCase("list")){
                    //CHECK ATTRIBUTE
                    if(attribute2.length == 2 && attribute2[0].equalsIgnoreCase("classtag")){
                        //GET LIST AND RETURN IT
                        Student[] students = DatabaseConnector.getStudentsByClassList(attribute2[1]);
                        if(students == null){return APIError.USER_NOT_FOUND_ERROR.toString();}
                        return createResponse("studentlist", new Gson().toJson(students));
                    }else return APIError.INVALID_GET_REQUEST_ERROR.toString();
                }else{
                    //RETURN STUDENT BY NAME
                    if(attribute1.length != 2 || attribute2.length != 2) return APIError.INVALID_GET_REQUEST_ERROR.toString();
                    //CHECK FORMAT
                    if(attribute1[0].equalsIgnoreCase("firstname") && attribute2[0].equalsIgnoreCase("lastname")){
                        //GET STUDENT FROM DATABASE
                        Student student = DatabaseConnector.getStudentByName(attribute1[1], attribute2[1]);
                        //CHECK POSITIVE RESULT OR NOT
                        if(student.isDataSet()) return createResponse("student", new Gson().toJson(student));
                        return APIError.USER_NOT_FOUND_ERROR.toString();
                    }else return APIError.INVALID_GET_REQUEST_ERROR.toString();
                }
            }else if(datapacks.length == 3){
                String[] attribute1 = datapacks[0].split("=");
                String[] attribute2 = datapacks[1].split("=");
                String[] attribute3 = datapacks[2].split("=");
                //CHECK FORMAT FOR LIST
                if(attribute1[0].equalsIgnoreCase("list") && attribute2[0].equalsIgnoreCase("classtag") && attribute3[0].equalsIgnoreCase("sort-by") && attribute2.length == 2 && attribute3.length == 2){
                    Student[] students = DatabaseConnector.getStudentsByClassListSorted(attribute2[1], attribute3[1]);
                    if(students == null){return APIError.USER_NOT_FOUND_ERROR.toString();}
                    return createResponse("studentlist", new Gson().toJson(students));
                }else{
                    return APIError.INVALID_GET_REQUEST_ERROR.toString();
                }
            }
        }
        return APIError.INVALID_GET_REQUEST_ERROR.toString();
    }


    private static String createResponse(String item_name, String json){
        return "{ \n" +
                "   \"success\":\"true\", \n" +
                "   \"" + item_name + "\":" +
                " " +json + "\n" +
                "}";
    }

}