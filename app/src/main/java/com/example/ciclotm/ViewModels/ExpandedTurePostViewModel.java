package com.example.ciclotm.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.ciclotm.Models.Posts.turePost;
import com.example.ciclotm.Repositories.ExpandedTurePostRepository;

import java.util.List;

public class ExpandedTurePostViewModel extends ViewModel {
    private ExpandedTurePostRepository mExpandedTurePostRepo;
    public void addParticipant(String uid, turePost post , List<String> participants) {
        mExpandedTurePostRepo = ExpandedTurePostRepository.getInstance();
        mExpandedTurePostRepo.addParticipant(uid,post,participants);
    }
}
