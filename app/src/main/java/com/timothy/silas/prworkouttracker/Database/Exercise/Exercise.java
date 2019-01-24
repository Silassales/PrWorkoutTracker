package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
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

    public Exercise(Integer id, String name, Double weight, WtUnit weightUnit) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.weightUnit = weightUnit;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) &&
                Objects.equals(name, exercise.name) &&
                Objects.equals(weight, exercise.weight) &&
                weightUnit == exercise.weightUnit;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, weight, weightUnit);
    }
}
