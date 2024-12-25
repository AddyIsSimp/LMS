package Entity;

public class Staff {
    private int StaffID;
    private String fName;
    private String lName;
    private String email;
    private String username;
    private String pass;

    public Staff() {
        this.StaffID = StaffID;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.pass = pass;
    }

    public Staff(int staffID, String fName, String lName, String email, String username, String pass) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.pass = pass;
        this.username = username;
        this.StaffID = staffID;
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
