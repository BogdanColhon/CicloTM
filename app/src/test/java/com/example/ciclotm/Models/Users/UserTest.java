package com.example.ciclotm.Models.Users;

import static org.junit.Assert.*;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class UserTest {
    Date data = new Date();
    User fake_user = new User("Maxim", "Cristian", data, "0756082099", "", "nbat9989@yahoo.com", "Bio", "M", "123.com", "1", "123", 0);


//    @Test
//    public void userEmailIsValid(){
//        boolean result = fake_user.userEmailIsValid("nbat9989@yahoo.com");
//        assertEquals(result,true);
//    }

    @Test
    public void userPhoneIsValid(){
        boolean result = fake_user.userPhoneIsValid("0756082099");
        assertEquals(result,true);
    }


    @Test
    public void getLastName() {
        assertEquals(fake_user.getLastName(), "Maxim");
    }

    @Test
    public void setLastName() {
        fake_user.setLastName("x");
        assertEquals(fake_user.getLastName(), "x");
    }

    @Test
    public void getFirstName() {
        assertEquals(fake_user.getFirstName(), "Cristian");
    }

    @Test
    public void setFirstName() {
        fake_user.setFirstName("x");
        assertEquals(fake_user.getFirstName(), "x");
    }

    @Test
    public void getPhoneNumber() {
        assertEquals(fake_user.getPhoneNumber(), "0756082099");
    }

    @Test
    public void setPhoneNumber() {
        fake_user.setPhoneNumber("0123456789");
        assertEquals(fake_user.getPhoneNumber(), "0123456789");
    }

    @Test
    public void getEmail() {
        assertEquals(fake_user.getEmail(), "nbat9989@yahoo.com");
    }

    @Test
    public void setEmail() {
        fake_user.setEmail("123@yahoo.com");
        assertEquals(fake_user.getEmail(), "123@yahoo.com");
    }

    @Test
    public void getBio() {
        assertEquals(fake_user.getBio(), "Bio");
    }

    @Test
    public void setBio() {
        fake_user.setBio("x");
        assertEquals(fake_user.getBio(), "x");
    }

    @Test
    public void getGender() {
        assertEquals(fake_user.getGender(), "M");
    }

    @Test
    public void setGender() {
        fake_user.setGender("x");
        assertEquals(fake_user.getGender(), "x");
    }

    @Test
    public void getProfileImageUrl() {
        assertEquals(fake_user.getProfileImageUrl(), "123.com");
    }

    @Test
    public void setProfileImageUrl() {
        fake_user.setProfileImageUrl("x");
        assertEquals(fake_user.getProfileImageUrl(),"x");
    }

    @Test
    public void getIsAdmin() {
        assertEquals(fake_user.getIsAdmin(), "1");
    }

    @Test
    public void setIsAdmin() {
        fake_user.setIsAdmin("2");
        assertEquals(fake_user.getIsAdmin(),"2");
    }

    @Test
    public void getUserId() {
        assertEquals(fake_user.getUserId(), "123");
    }

    @Test
    public void setUserId() {
        String id = "abcdef";
        User user = new User();
        user.setUserId(id);
        assertEquals(user.getUserId(), id);
    }

    @Test
    public void getStatus() {
        assertEquals(fake_user.getStatus(),0);
    }

    @Test
    public void setStatus() {
        int status = 1;
        User user = new User();
        user.setStatus(status);
        assertEquals(user.getStatus(), status);
    }
}