package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.DialogFragmentLiveRepository;

import java.util.Calendar;
import java.util.Date;

public class DialogFragmentLiveViewModel extends ViewModel {
    private DialogFragmentLiveRepository mDialogFragmentLiveRepo;

    public void setMarker(String val1, String val2, Double val3, Double val4) {
        mDialogFragmentLiveRepo = DialogFragmentLiveRepository.getInstance();

        Date currentTime = Calendar.getInstance().getTime();
        Date expiringTime = new Date();
        expiringTime.setTime(System.currentTimeMillis() + (6 * 60 * 60 * 1000));

        mDialogFragmentLiveRepo.setMarker(val1, val2, val3, val4, currentTime, expiringTime);
    }
}
