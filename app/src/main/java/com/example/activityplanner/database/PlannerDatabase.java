package com.example.activityplanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.activityplanner.database.dao.ActivityDAO;
import com.example.activityplanner.database.entities.Activity;
import com.example.activityplanner.database.entities.Picture;
import com.example.activityplanner.util.ActivityTypeRoomConverter;
import com.example.activityplanner.util.Constants;
import com.example.activityplanner.util.DateRoomConverter;

@Database(entities = { Activity.class, Picture.class }, version = 1)
@TypeConverters({ DateRoomConverter.class, ActivityTypeRoomConverter.class })
public abstract class PlannerDatabase extends RoomDatabase {

    public abstract ActivityDAO getActivityDAO();

    private static PlannerDatabase plannerDatabase;

    // synchronized is use to avoid concurrent access in multi thread environment
    public static /*synchronized*/ PlannerDatabase getInstance(Context context) {
        if (null == plannerDatabase) {
            plannerDatabase = buildDatabaseInstance(context);
        }
        return plannerDatabase;
    }

    @NonNull
    private static PlannerDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                PlannerDatabase.class,
                Constants.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        plannerDatabase = null;
    }
}
