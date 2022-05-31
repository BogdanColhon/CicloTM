package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Repositories.TurePostRepository;

import java.util.Date;
import java.util.List;

public class TurePostViewModel extends ViewModel {
    private TurePostRepository mTurePostRepo;

    public void postData(String title, String distance, String duration,String timeUnit, String startTime, String startPoint, int no_participants, String description, String uid, Date currentTime, Date activityDate, List<String> participants) {
        mTurePostRepo = TurePostRepository.getInstance();
        mTurePostRepo.postData(title,distance,duration,timeUnit, startTime,startPoint,no_participants,description,uid,currentTime,activityDate,participants);
    }
}
