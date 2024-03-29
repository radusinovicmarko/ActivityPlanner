package org.unibl.etf.mr.activityplanner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.unibl.etf.mr.activityplanner.util.Constants;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = Constants.TABLE_NAME_PICTURE)
public class Picture implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String uri;

    @ColumnInfo(name = "activity_id")
    private long activityId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public Picture(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return id == picture.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
