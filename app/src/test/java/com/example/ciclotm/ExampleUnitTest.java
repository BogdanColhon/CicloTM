package com.example.ciclotm;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.ciclotm.Models.Objects.Location;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    Location location  = new Location(43.15,20.14);
    @Test
    public void getLatitude(){
        assertNotEquals(location.getLatitude(),413.15);
        assertNotEquals(location.getLongitude(),207.14);
    }
}