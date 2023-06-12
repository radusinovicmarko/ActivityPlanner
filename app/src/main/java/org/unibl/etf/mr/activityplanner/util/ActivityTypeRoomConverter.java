package org.unibl.etf.mr.activityplanner.util;

import androidx.room.TypeConverter;

import org.unibl.etf.mr.activityplanner.database.enums.ActivityType;

public class ActivityTypeRoomConverter {

    @TypeConverter
    public static ActivityType toActivityType(Byte value) { return value == null ? null : ActivityType.values()[value]; }

    @TypeConverter
    public static Byte fromActivityType(ActivityType value) { return  value == null ? null : (byte) value.ordinal(); }
}
