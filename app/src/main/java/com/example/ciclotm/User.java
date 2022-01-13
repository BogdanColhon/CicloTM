package com.example.ciclotm;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private String LastName;
    private String FirstName;
    private String PhoneNumber;
    private String PhoneId;
    private String Email;
    private String Bio;
    private String Gender;
    private String profileImageUrl;
    private String isAdmin;
    private Date BirthDate;

    public User() {
    }

    public User(String LastName, String FirstName, Date BirthDate, String PhoneNumber, String PhoneId, String Email, String Bio, String Gender, String profileImageUrl,String isAdmin) {
        this.LastName = LastName;
        this.FirstName = FirstName;
        this.BirthDate = BirthDate;
        this.PhoneNumber = PhoneNumber;
        this.PhoneId = PhoneId;
        this.Email = Email;
        this.Bio = Bio;
        this.Gender = Gender;
        this.profileImageUrl = profileImageUrl;
        this.isAdmin = isAdmin;
    }


    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPhoneId() {
        return PhoneId;
    }

    public void setPhoneId(String phoneId) {
        PhoneId = phoneId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public Date getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

}
