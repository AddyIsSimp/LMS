package Entity;

public class Staff {
    private int StaffID;
    private String fName;
    private String lName;
    private String username;
    private String pass;

    public Staff() {
        this.StaffID = StaffID;
        this.fName = fName;
        this.lName = lName;
        this.username = username;
        this.pass = pass;
    }

    public Staff(int staffID, String fName, String lName, String username, String pass) {
        this.fName = fName;
        this.lName = lName;
        this.pass = pass;
        this.username = username;
        this.StaffID = staffID;
    }

    public Staff(String fName, String lName, String username, String pass) {
        this.fName = fName;
        this.lName = lName;
        this.pass = pass;
        this.username = username;
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

    public String getPassword() {
        return pass;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public int getStaffId() {
        return StaffID;
    }

    public void setStaffId(int StaffID) {
        this.StaffID = StaffID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
