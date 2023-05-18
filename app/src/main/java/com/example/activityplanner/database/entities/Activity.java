package com.example.activityplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.activityplanner.database.enums.ActivityType;
import com.example.activityplanner.util.Constants;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = Constants.TABLE_NAME_ACTIVITY)
public class Activity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;

    private String description;

    private Date date;

    @ColumnInfo(name = "location_name")
    private String locationName;

    @ColumnInfo(name = "location_latitude")
    private Double locationLatitude;

    @ColumnInfo(name = "location_longitude")
    private Double locationLongitude;

    private ActivityType type;

    private boolean deleted;

    public Activity(String title, String description, Date date, String locationName, Double locationLatitude, Double locationLongitude, ActivityType type) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.locationName = locationName;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return id == activity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
