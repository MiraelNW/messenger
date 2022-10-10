package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private String currUserId;
    private String otherUserId;

    public ChatViewModelFactory(String currUserId, String otherUserId) {
        this.currUserId = currUserId;
        this.otherUserId = otherUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currUserId,otherUserId);
    }
}
