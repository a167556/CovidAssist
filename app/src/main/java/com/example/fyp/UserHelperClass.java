package com.example.fyp;

public class UserHelperClass {

    String fullName, phoneNo, icNo, userAddress, userAid, uid;



    public UserHelperClass() {
    }

    public UserHelperClass(String fullName, String phoneNo, String icNo, String userAddress, String userAid, String uid) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.icNo = icNo;
        this.userAddress = userAddress;
        this.userAid = userAid;
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullname) {
        this.fullName = fullname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserAid() {
        return userAid;
    }

    public void setUserAid(String userAid) {
        this.userAid = userAid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
