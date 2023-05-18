package com.example.activityplanner.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<String> testText;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        testText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}