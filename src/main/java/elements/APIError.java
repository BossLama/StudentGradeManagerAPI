package elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public enum APIError {

    NO_ERROR("Successful", false, "There is no error", 0),
    SYNTAX_ERROR("Syntax Error", true, "Please use the given syntax to use our API", 1),
    API_KEY_ERROR("API-Key Error", true, "No or an invalid API key was used", 2),
    UNKOWN_REQUEST_TYPE("Unknown RequestType", true, "RequestType is unknown", 3),
    WRONG_JSON_ERROR("Wrong JSON format",  true,"Check our documentation for the right format", 4),
    UNSET_ACTION_ERROR("Unset action Error", true, "Please define \"action\" in your JSON", 5),
    SAVA_DATABASE_ERROR("Database Error", true, "Couldn\"t save data in database", 6),
    UNKNOWN_DATA_NAME_ERROR("Unknown data-name", true, "JSON contains unknown data-names", 7),
    USER_NOT_FOUND_ERROR("Entry not found Error", true, "Entry doesn\"t exist", 8),
    INVALID_GET_REQUEST_ERROR("Invalid Get-Request", true, "Your Get-Request is not valid", 9);


    public String name;
    public String message;
    public int errorcode;
    public Boolean isError;

    APIError(String name, boolean isError, String message, int errorcode){
        this.name = name;
        this.message = message;
        this.errorcode = errorcode;
        this.isError = isError;
    }

    public String getName(){
        return name;
    }

    public String getMessage(){
        return message;
    }

    public int getErrorcode(){
        return errorcode;
    }

    public Boolean getError() {
        return isError;
    }

    public String toString(){
        String returning = "{ \n";
        returning += "\"success\":\"" + !isError + "\", \n";
        returning += "\"error\": { \n";
        returning += "  \"name\":\"" + name + "\", \n";
        returning += "  \"message\":\"" + message + "\", \n";
        returning += "  \"errorcode\":\"" + errorcode + "\" \n";
        returning += "}";
        return returning;
    }

}
