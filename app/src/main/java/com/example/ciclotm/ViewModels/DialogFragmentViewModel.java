package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.DialogFragmentRepository;

public class DialogFragmentViewModel extends ViewModel {
    private DialogFragmentRepository mDialogFragmentRepo;

    public void setMarker(String val1, String val2, Double val3, Double val4) {
        mDialogFragmentRepo = DialogFragmentRepository.getInstance();
        mDialogFragmentRepo.setMarker(val1, val2, val3, val4);
    }
}
