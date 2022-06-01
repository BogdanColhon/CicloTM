package com.example.ciclotm.Repositories;

import com.example.ciclotm.Models.Markers.LiveEventsMarker;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class DialogFragmentLiveRepository {
    public static DialogFragmentLiveRepository instance;

    public static DialogFragmentLiveRepository getInstance() {
        if (instance == null) {
            instance = new DialogFragmentLiveRepository();
        }
        return instance;
    }

    public void setMarker(String val1, String val2, Double val3, Double val4, Date currentTime, Date expiringTime) {
        LiveEventsMarker marker = new LiveEventsMarker(val1, val1, val2, currentTime, expiringTime, val3, val4, 0);

        FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("LiveEventsMarkers").child(String.valueOf(currentTime))
                .setValue(marker);
    }
}
