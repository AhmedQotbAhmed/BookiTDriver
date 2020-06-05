package com.example.bookitdriver.model;

public class Driver {

    private String fname;
    private String lname;
    private String email;
    private String password;
    private String mobile;
    private String address;
    private String gander;








    public Driver() {

    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }
    public String getAddress() {
        return address;
    }

    public String getGander() {
        return gander;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setGander(String gander) {
        this.gander = gander;
}

    public Driver(String fname, String lname, String email, String password, String mobile, String address, String gander) {

        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.address = address;
        this.gander = gander;
    }
}
