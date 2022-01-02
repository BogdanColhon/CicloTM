package com.example.ciclotm;

import java.util.Date;

public class User {
    public String LastName,FirstName,PhoneNumber,PhoneId,Email;
    public Date BirthDate;

    public User(){}

    public User(String LastName,String FirstName,Date BirthDate, String PhoneNumber, String PhoneId, String Email){
        this.LastName=LastName;
        this.FirstName=FirstName;
        this.BirthDate=BirthDate;
        this.PhoneNumber=PhoneNumber;
        this.PhoneId=PhoneId;
        this.Email=Email;
    }
}
