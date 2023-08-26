package com.example.foodcorner.Models;


public class UserModel {
    private String Name,Email,MobileNo,Address;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public UserModel(String name,String email) {
        Name = name;
        Email = email;
    }
    public UserModel(String name, String email, String mobileNo, String address) {
        Name = name;
        Email = email;
        MobileNo = mobileNo;
        Address = address;
    }
    public UserModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}

