package database;

import com.google.gson.Gson;
import elements.APIError;
import elements.Grade;
import elements.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DatabaseConnector {

    private static final String host = "localhost";
    private static final int port = 3306;
    private static final String username = "root";
    private static final String password = "";
    private static final String database = "student_manager";

    public static Connection connection;

    public static void connect(){
        System.out.println("[i] Verbindet sich mit Datenbank...");
        String url = "jdbc:mysql://"+ host +":"+ port +"/"+ database +"?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Berlin";
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("[i] Erfolgreich mit Datenbank " + database + " verbunden!");
            System.out.println("[i] Datenbank - Überprüft Tables");
            setupTables();
            System.out.println("[i] Datenbank - Erfolgreich");
        } catch (SQLException e) {
            System.out.println("Error -> Keine Verbindung zur Datenbank hergestellt");
            e.printStackTrace();
        }
    }

    private static void setupTables(){
        String student_table_sql = "CREATE TABLE IF NOT EXISTS students (" +
                "student_id int NOT NULL AUTO_INCREMENT," +
                "firstname varchar(256)," +
                "lastname varchar(256)," +
                "classtag varchar(256)," +
                "PRIMARY KEY (student_id)" +
                ")";

        String grade_table_sql = "CREATE TABLE IF NOT EXISTS grades (" +
                "grade_id int NOT NULL AUTO_INCREMENT," +
                "student_id int," +
                "grade_type varchar(256)," +
                "subject varchar(256)," +
                "value double," +
                "weight int," +
                "date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (grade_id)" +
                ")";

        try {
            Statement statement = connection.createStatement();
            statement.execute(student_table_sql);
            statement.execute(grade_table_sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //============= POST - STUDENT - METHODS =============

    public static APIError createStudent(Student student){
        String sql = "INSERT INTO students (firstname, lastname, classtag) VALUES (" +
                "'" + student.getFirstname() +"',"+
                "'" + student.getLastname() +"',"+
                "'" + student.getClasstag() +"'"+
                ")";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return APIError.SAVA_DATABASE_ERROR;
        }
    }

    public static APIError updateStudent(String data_name, String data_value, String student_id){
        try{
            String sql = "UPDATE students SET " + data_name + "='" + data_value + "' WHERE student_id=" + student_id;
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        }catch (Exception e){
            return APIError.UNKNOWN_DATA_NAME_ERROR;
        }
    }

    public static APIError deleteStudent(String student_id){
        try{
            String sql = "DELETE FROM students WHERE student_id=" + student_id;
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        }catch (Exception e){
            return APIError.UNKNOWN_DATA_NAME_ERROR;
        }
    }


    //============ GET - STUDENT - METHODS ============

    public static Student getStudentByID(String student_id){
        Student student = new Student();
        System.out.println("Search for id " + student_id);
        String sql = "SELECT * FROM students WHERE student_id='" + student_id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                int sid = resultSet.getInt("student_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String classtag = resultSet.getString("classtag");

                student.setStudentid(sid);
                student.setFirstname(firstname);
                student.setLastname(lastname);
                student.setClasstag(classtag);
                student.setAverage(getAverage(sid + ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }

    public static Student getStudentByName(String firstname, String lastname){
        String sql = "SELECT * FROM students WHERE firstname='" + firstname + "' AND lastname='" + lastname + "'";
        Student student = new Student();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if(resultSet.next()){
                int sid = resultSet.getInt("student_id");
                String sfirstname = resultSet.getString("firstname");
                String slastname = resultSet.getString("lastname");
                String classtag = resultSet.getString("classtag");

                student.setStudentid(sid);
                student.setFirstname(sfirstname);
                student.setLastname(slastname);
                student.setClasstag(classtag);
                student.setAverage(getAverage(sid + ""));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }


    private static double getAverage(String student_id){
        String sql = "SELECT * FROM grades WHERE student_id='" + student_id + "'";
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            Integer amount = 0;
            Double sum = 0.0;

            while(result.next()){
                int weight = result.getInt("weight");
                double value = result.getDouble("value");
                amount++;
                for(int i = 0; i < weight; i++){
                   sum += value;
                }
            }
            if(amount == 0) return 0.0;
            return (sum) / amount;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0.0;
    }

    public static Student[] getStudentsByClassList(String classtag){
        try {
            String sql = "SELECT * FROM students WHERE classtag='" + classtag + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            ArrayList<Student> students = new ArrayList<>();
            while (resultSet.next()){
                int studentid = resultSet.getInt("student_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String sclasstag = resultSet.getString("classtag");

                Student student = new Student(studentid, firstname, lastname, sclasstag);
                student.setAverage(getAverage(studentid + ""));
                students.add(student);
            }
            Student[] array = new Student[students.size()];
            for(int i = 0; i < students.size(); i++){
                array[i] = students.get(i);
            }
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Student[] getStudentsByClassListSorted(String classtag, String sortid){
        try {
            String sql = "";
            if(!sortid.equalsIgnoreCase("average")){
                sql = "SELECT * FROM students WHERE classtag='" + classtag + "' ORDER BY " + sortid + " ASC";
            }else{
                sql = "SELECT * FROM students WHERE classtag='" + classtag + "'";
            }
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            //GET ALL STUDENTS OF CLASS
            ArrayList<Student> students = new ArrayList<>();
            while (resultSet.next()){
                int studentid = resultSet.getInt("student_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String sclasstag = resultSet.getString("classtag");
                Student student = new Student(studentid, firstname, lastname, sclasstag);
                student.setAverage(getAverage(studentid + ""));
                students.add(student);
            }

            if(sortid.equalsIgnoreCase("average")) Collections.sort(students);

            //ARRAYLIST TO ARRAY
            Student[] array = new Student[students.size()];
            for(int i = 0; i < students.size(); i++){
                array[i] = students.get(i);
            }
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    //=========== POST GRADE METHODS ========

    public static APIError createGrade(Grade grade){
        String sql = "INSERT INTO grades (student_id, grade_type, subject, value, weight) VALUES (" +
                "" + grade.getId() +","+
                "'" + grade.getGradeType() +"',"+
                "'" + grade.getSubject() +"',"+
                "" + grade.getValue() +","+
                "" + grade.getWeight() +""+
                ")";
        System.out.println(sql);
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        } catch (SQLException e) {
            e.printStackTrace();
            return APIError.SAVA_DATABASE_ERROR;
        }
    }

    public static APIError updadeGrade(String gradeid, String attribute, String value){
        String sql = "UPDATE grades SET " + attribute + "='" + value + "'";
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        }catch (SQLException e){
            e.printStackTrace();
            return APIError.SAVA_DATABASE_ERROR;
        }
    }

    public static APIError deleteGrade(String gradeid){
        try{
            String sql = "DELETE FROM grade WHERE grade=" + gradeid;
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return APIError.NO_ERROR;
        }catch (Exception e){
            return APIError.UNKNOWN_DATA_NAME_ERROR;
        }
    }

    //============ GET REQUEST GRADES ==========

    public static Grade getGradeByID(String gradid){
        String sql = "SELECT * FROM grades WHERE grade-id='" + gradid + "'";
        try{
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            Grade grade = new Grade();
            while(set.next()){
                Integer grade_id = set.getInt("grade_id");
                Integer student_id = set.getInt("student_id");
                String grade_type = set.getString("grade-type");
                String subject = set.getString("subject");
                Double value = set.getDouble("value");
                Integer weight = set.getInt("weight");

                grade.setId(student_id);
                grade.setOwnID(grade_id);
                grade.setValue(value);
                grade.setWeight(weight);
                grade.setGradeType(grade_type);
                grade.setSubject(subject);
            }

            return grade;
        }catch (Exception e){
            return null;
        }
    }

    public static Grade[] getGradesByStudentid(String studentid){
        ArrayList<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id=" + studentid + "";
        try{
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);

            while(set.next()){
                Grade grade = new Grade();
                grade.setId(set.getInt("student_id"));
                grade.setOwnID(set.getInt("grade_id"));
                grade.setSubject(set.getString("subject"));
                grade.setWeight(set.getInt("weight"));
                grade.setValue(set.getDouble("value"));
                grade.setGradeType("grade_type");
                grades.add(grade);

            }

            if(grades.size() == 0) return null;
            Grade[] returning = new Grade[grades.size()];
            for(int i = 0; i < grades.size(); i++){
                returning[i] = grades.get(i);
            }
            return returning;

        }catch (Exception e){
            return null;
        }
    }
}
