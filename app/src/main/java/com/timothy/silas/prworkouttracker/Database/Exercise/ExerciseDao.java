package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Utils.UUIDConverter;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM exercise")
    LiveData<List<Exercise>> getAll();

    @Query("SELECT * FROM exercise where id = :id")
    @TypeConverters(UUIDConverter.class)
    Exercise getById(UUID id);

    @Query("SELECT * FROM exercise where (:name) = name")
    Exercise getByName(String name);

    @Query("UPDATE exercise SET weight = (:newWeight) where id = (:id)")
    @TypeConverters(UUIDConverter.class)
    void updateWeight(UUID id, Double newWeight);

    @Insert(onConflict = REPLACE)
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);
}