package com.timothy.silas.prworkouttracker.Database.Category;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    LiveData<List<Category>> getAll();

    @Query("SELECT * FROM category")
    List<Category> getAllBasic();

    @Query("SELECT * FROM category where (:name) = name")
    Category getByName(String name);

    @Insert(onConflict = REPLACE)
    void insert(Category category);

    @Delete
    void delete(Category category);
}
