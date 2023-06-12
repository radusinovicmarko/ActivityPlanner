package org.unibl.etf.mr.activityplanner.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import org.unibl.etf.mr.activityplanner.database.dao.ActivityDAO;
import org.unibl.etf.mr.activityplanner.database.entities.Activity;
import org.unibl.etf.mr.activityplanner.database.entities.Picture;
import org.unibl.etf.mr.activityplanner.util.ActivityTypeRoomConverter;
import org.unibl.etf.mr.activityplanner.util.Constants;
import org.unibl.etf.mr.activityplanner.util.DateRoomConverter;

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
