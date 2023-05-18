package com.example.activityplanner.database.dto;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.activityplanner.database.entities.Activity;
import com.example.activityplanner.database.entities.Picture;

import java.util.List;

public class ActivityWithPictures {

    @Embedded
    private Activity activity;

    @Relation(parentColumn = "id", entityColumn = "activity_id")
    private List<Picture> pictures;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
