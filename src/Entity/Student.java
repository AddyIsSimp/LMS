package Entity;

import static Function.globalVariable.fnc;

public class Student {
    private int studentID;
    private int fullName;
    private String fName;
    private String lName;
    private String section;
    private String email;
    private String pass;
    private double penalty;
    private int schoolID;

    public Student() {
        this.schoolID = schoolID;
        this.fName = fName;
        this.lName = lName;
        this.section = section;
        this.email = email;
        this.pass = pass;
        this.penalty = penalty;
    }

    public Student (int schoolID, String fName, String lName, String section, String email, String pass) {
        this.schoolID = schoolID;
        this.fName = fName;
        this.lName = lName;
        this.section = section;
        this.email = email;
        this.pass = pass;
        this.penalty = penalty;
        fnc.getFullName(fName, lName);
    }

    public Student (int schoolID, String fName, String lName, String section, String email, String pass, double penalty) {
        this.schoolID = schoolID;
        this.fName = fName;
        this.lName = lName;
        this.section = section;
        this.email = email;
        this.pass = pass;
        this.penalty = penalty;
        fnc.getFullName(fName, lName);
    }


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getSchoolID() {
        return schoolID;
    }

    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }


    public int getFullName() {
        if(fName!=null && lName!=null ) fnc.getFullName(fName, lName);
        return fullName;
    }

    public void setFullName(int fullName) {
        this.fullName = fullName;
    }
}
