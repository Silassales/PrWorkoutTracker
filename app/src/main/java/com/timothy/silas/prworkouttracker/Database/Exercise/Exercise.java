package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.SET_NULL;

@Entity(foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "category_id", onDelete = SET_NULL))
public class Exercise implements Serializable {
    private static final long serialVersionUID = -1L;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "weight")
    private Double weight;

    @ColumnInfo(name = "unit")
    @TypeConverters(WtUnitConverter.class)
    private WtUnit weightUnit;

    @ColumnInfo(name = "category_id")
    private Integer categoryId;

    public Exercise(Integer id, String name, Double weight, WtUnit weightUnit, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getWeight() {
        return weight;
    }

    public WtUnit getWeightUnit() {
        return weightUnit;
    }

    public Integer getCategoryId() { return  categoryId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) &&
                Objects.equals(name, exercise.name) &&
                Objects.equals(weight, exercise.weight) &&
                weightUnit == exercise.weightUnit &&
                Objects.equals(categoryId, exercise.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, weightUnit, categoryId);
    }
}
