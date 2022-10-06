package elements;

public class Student implements Comparable<Student> {

    private int studentid;
    private String firstname;
    private String lastname;
    private String classtag;
    private double average;


    public Student(int studentid, String firstname, String lastname, String classtag) {
        this.studentid = studentid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.classtag = classtag;
    }

    public Student(){

    }

    public void setAverage(double average){
        this.average = average;
    }

    public double getAverage(){
        return this.average;
    }

    public boolean isDataSet(){
        if(firstname == null) return false;
        if(lastname == null) return false;
        if(classtag == null) return false;

        if(firstname == "") return false;
        if(lastname == "") return false;
        if(classtag == "") return false;

        return true;
    }

    public int getStudentid() {
        return studentid;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public String getFirstname() {
        return firstname.replace("&", "").
                replace("=", "").
                replace("!", "").
                replace(";", "");
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname.replace("&", "").
                replace("=", "").
                replace("!", "").
                replace(";", "");
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getClasstag() {
        return classtag.replace("&", "").
                replace("=", "").
                replace("!", "").
                replace(";", "");
    }

    public void setClasstag(String classtag) {
        this.classtag = classtag;
    }

    @Override
    public int compareTo(Student o) {
        if(this.average < o.getAverage()){
            return -1;
        }else if(this.average == o.getAverage()){
            return 0;
        }else return 1;
    }
}
