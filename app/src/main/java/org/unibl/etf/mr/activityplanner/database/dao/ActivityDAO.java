package org.unibl.etf.mr.activityplanner.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;
import org.unibl.etf.mr.activityplanner.database.entities.Activity;
import org.unibl.etf.mr.activityplanner.database.entities.Picture;

import java.util.Date;
import java.util.List;

@Dao
public interface ActivityDAO {

    @Transaction
    @Query("SELECT * FROM activity a ORDER BY a.date")
    List<ActivityWithPictures> getAllActivities();

    @Transaction
    @Query("SELECT * FROM activity a WHERE a.title LIKE '%' || :title || '%' AND a.date >= :from ORDER BY a.date")
    List<ActivityWithPictures> getAllByTitle(String title, Date from);

    @Transaction
    @Query("SELECT * FROM activity a WHERE a.date >= :from AND a.date < :to")
    List<ActivityWithPictures> getAllByDate(Date from, Date to);

    @Transaction
    @Query("SELECT * FROM activity a WHERE a.date >= :from ORDER BY a.date")
    List<ActivityWithPictures> getAllByDate(Date from);

    @Transaction
    @Insert
    long insertActivity(Activity activity);

    @Transaction
    @Insert
    void insertPicture(Picture picture);

    @Transaction
    @Delete
    void deleteActivity(Activity activity);
}
