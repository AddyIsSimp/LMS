package Entity;

import static Function.globalVariable.fnc;

public class Student {
    private String fullName;
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
        this.fullName = fnc.getFullName(fName, lName);
    }



    public Student (int schoolID, String fName, String lName, String section, String email, String pass, double penalty) {
        this.schoolID = schoolID;
        this.fName = fName;
        this.lName = lName;
        this.section = section;
        this.email = email;
        this.pass = pass;
        this.penalty = penalty;
        this.fullName = fnc.getFullName(fName, lName);
    }

    public Student(int schoolID, String sectionID, String fName, String lName, String password) {
        
    }


    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
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



    public String getFullName() {
        if(fName!=null && lName!=null ) this.fullName = fnc.getFullName(fName, lName);
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
