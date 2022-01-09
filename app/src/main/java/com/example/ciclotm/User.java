package com.example.ciclotm;

import java.util.Date;

public class User {
    public String LastName,FirstName,PhoneNumber,PhoneId,Email,Bio,Gender;
    public Date BirthDate;

    public User(){}

    public User(String LastName,String FirstName,Date BirthDate, String PhoneNumber, String PhoneId, String Email,String Bio, String Gender){
        this.LastName=LastName;
        this.FirstName=FirstName;
        this.BirthDate=BirthDate;
        this.PhoneNumber=PhoneNumber;
        this.PhoneId=PhoneId;
        this.Email=Email;
        this.Bio=Bio;
        this.Gender=Gender;
    }
}
