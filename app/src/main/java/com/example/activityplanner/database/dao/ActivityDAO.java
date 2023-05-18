package com.example.activityplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.database.entities.Activity;

import java.util.List;

@Dao
public interface ActivityDAO {

    @Transaction
    @Query("SELECT * FROM activity")
    List<ActivityWithPictures> getAllActivities();

    @Transaction
    @Query("SELECT * FROM activity a WHERE a.title LIKE '%' || :title || '%'")
    List<ActivityWithPictures> getAllByTitle(String title);

    @Transaction
    @Insert
    void insertActivity(Activity activity);
}
