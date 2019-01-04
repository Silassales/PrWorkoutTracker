package com.timothy.silas.prworkouttracker.Database.Exercise;

import com.timothy.silas.prworkouttracker.Database.Utils.UUIDConverter;
import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Exercise {
    @PrimaryKey
    @TypeConverters(UUIDConverter.class)
    @NonNull
    public UUID id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "weight")
    public Double weight;

    @ColumnInfo(name = "unit")
    @TypeConverters(WtUnitConverter.class)
    public WtUnit weightUnit;

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
