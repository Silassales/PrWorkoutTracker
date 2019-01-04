package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Utils.UUIDConverter;

import java.util.List;
import java.util.UUID;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    List<Exercise> getAll();

    @Query("SELECT * FROM exercise where id = :id")
    @TypeConverters(UUIDConverter.class)
    Exercise getById(UUID id);

    @Query("SELECT * FROM exercise where name in (:names)")
    List<Exercise> getAllByName(List<String> names);

    @Insert
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);
}
