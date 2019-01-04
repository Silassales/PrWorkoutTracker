package com.timothy.silas.prworkouttracker.Database.Utils;

import java.util.UUID;

import androidx.room.TypeConverter;

public class UUIDConverter {

    @TypeConverter
    public static UUID toUUID(String uuid) {
        return uuid == null ? null : UUID.fromString(uuid);
    }

    @TypeConverter
    public static String toString(UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

}
