package api.graderequest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseConnector;
import elements.APIError;
import elements.Grade;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GradeRequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String request_method = exchange.getRequestMethod();
        String response = "";

        //KEY FOR SYSTEMCHECK
        if(exchange.getRequestURI().toString().equalsIgnoreCase("/api/grade?check")){
            System.out.println("[i] Systemcheck erhalten - Grades");
            response = "positive - " + exchange.getRequestMethod();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        //EXECUTE DIFFERENT HANDLER (POST | GET)
        if(request_method.equalsIgnoreCase("post")){
            response = handlePost(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8), exchange);
        }else if(request_method.equalsIgnoreCase("get")){
            response = handleGet(exchange.getRequestURI().toString(), exchange);
        }else response = APIError.UNKOWN_REQUEST_TYPE.toString();

        //SENDING RESPONSE TO CLIENT
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static String handlePost(String data, HttpExchange exchange){
        //EXECUTED BY POST-REQUEST
        try{
            JSONObject jsondata = new JSONObject(data);
            if(jsondata.getString("action").equalsIgnoreCase("create")){
                //CREATE NEW GRADE
                jsondata = jsondata.getJSONObject("data");
                //LOAD DATA
                String studentid = jsondata.getString("student-id");
                String gradetype = jsondata.getString("grade-type");
                String subject = jsondata.getString("subject");
                Double value = jsondata.getDouble("value");
                Integer weight = jsondata.getInt("weight");
                //SAVE GRADE
                Grade grade = new Grade(Integer.parseInt(studentid), gradetype, subject, value, weight);
                return  DatabaseConnector.createGrade(grade).toString();
            }else if(jsondata.getString("action").equalsIgnoreCase("update")){
                //UPDATE GRADE
                String gradeid = jsondata.getString("grade-id");
                //GET DATA VALUES AND SEND TO DATABASE
                JSONObject datas = jsondata.getJSONObject("data");
                Grade grades = new Grade();
                if(datas.has("student-id")){
                    DatabaseConnector.updadeGrade(gradeid, "student-id", datas.getString("student-id"));
                }
                if(datas.has("grade-type")){
                    DatabaseConnector.updadeGrade(gradeid, "grade-type", datas.getString("grade-type"));
                }
                if(datas.has("subject")){
                    DatabaseConnector.updadeGrade(gradeid, "subject", datas.getString("subject"));
                }
                if(datas.has("value")){
                    DatabaseConnector.updadeGrade(gradeid, "value", datas.getString("value"));
                }
                if(datas.has("weight")){
                    DatabaseConnector.updadeGrade(gradeid, "weight", datas.getString("weight"));
                }
                return APIError.NO_ERROR.toString();
            }else if(jsondata.getString("action").equalsIgnoreCase("delete")){
                //DELETE GRADE
                String gradeid = jsondata.getString("grade-id");
                return DatabaseConnector.deleteGrade(gradeid).toString();
            }else return APIError.UNSET_ACTION_ERROR.toString();
        }catch (Exception e){
            return APIError.WRONG_JSON_ERROR.toString();
        }
    }

    public static String handleGet(String data, HttpExchange exchange){
        //EXECUTED BY GET-REQUEST
        //SAVE AND CHECK DATAS
        String[] datasets = data.split("\\?");
        if(datasets.length != 2) return APIError.INVALID_GET_REQUEST_ERROR.toString();
        datasets = datasets[1].split("&");

        if(datasets.length == 1){
            String[] attribute = datasets[0].split("=");
            if(attribute.length != 2) return APIError.INVALID_GET_REQUEST_ERROR.toString();

            switch (attribute[0]){
                default:
                    return APIError.INVALID_GET_REQUEST_ERROR.toString();
                case ("grade-id"):
                    Grade grade = DatabaseConnector.getGradeByID(attribute[1]);
                    if(grade == null) return APIError.SQL_ERROR.toString();
                    return createResponse("grade", new Gson().toJson(grade));
                case("student-id"):
                    String studentid = attribute[1];
                    Grade[] grades = DatabaseConnector.getGradesByStudentid(studentid);
                    if(grades == null) return APIError.SQL_ERROR.toString();
                    return createResponse("grades", new Gson().toJson(grades));

            }
        }

        return "";
    }


    private static String createResponse(String item_name, String json){
        return "{ \n" +
                "   \"success\":\"true\", \n" +
                "   \"" + item_name + "\":" +
                " " +json + "\n" +
                "}";
    }
}
