package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Category.Category;

import java.util.Calendar;
import java.util.List;

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
    Exercise getById(Integer id);

    @Query("SELECT * FROM exercise where (:name) = name")
    Exercise getByName(String name);

    @Query("SELECT * FROM exercise where category_id = (:categoryId)")
    List<Exercise> getByCategory(Integer categoryId);

    @Query("UPDATE exercise SET weight = (:newWeight) where id = (:id)")
    void updateWeight(Integer id, Double newWeight);

    @Query("UPDATE exercise SET category_id = (:categoryId) where id = (:id)")
    void changeCategory(Integer id, Integer categoryId);

    @Insert(onConflict = REPLACE)
    void insert(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("DELETE FROM exercise")
    void nukeTable();
}
