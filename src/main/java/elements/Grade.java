package elements;

import java.sql.Timestamp;

public class Grade {

    private int ownID;
    private int id;
    private String gradeType;
    private String subject;
    private Double value;
    private Integer weight;

    public Grade() {

    }

    public Grade(int id, String gradeType, String subject, Double value, Integer weight) {
        this.id = id;
        this.gradeType = gradeType;
        this.subject = subject;
        this.value = value;
        this.weight = weight;
    }

    public int getOwnID() {
        return ownID;
    }

    public void setOwnID(int ownID) {
        this.ownID = ownID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGradeType() {
        return gradeType.replace("&", "").
                replace("=", "").
                replace("!", "").
                replace(";", "");
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getSubject() {
        return subject.replace("&", "").
                replace("=", "").
                replace("!", "").
                replace(";", "");
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
