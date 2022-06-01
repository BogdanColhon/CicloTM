package com.example.ciclotm.Repositories;

import com.example.ciclotm.Models.Markers.PointOfInterestMarker;
import com.google.firebase.database.FirebaseDatabase;

public class DialogFragmentRepository {
    public static DialogFragmentRepository instance;

    public static DialogFragmentRepository getInstance() {
        if (instance == null) {
            instance = new DialogFragmentRepository();
        }
        return instance;
    }

    public void setMarker(String val1, String val2, Double val3, Double val4) {

        PointOfInterestMarker marker = new PointOfInterestMarker(val1, val2, val3, val4);
        FirebaseDatabase.getInstance("https://ciclotm-default-rtdb.europe-west1.firebasedatabase.app").getReference("PointsOfInterestMarkers").child(val1)
                .setValue(marker);
    }
}
