package com.timothy.silas.prworkouttracker.Database.Category;

import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.io.Serializable;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = -1L;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "category_id")
    private Integer id;

    @ColumnInfo(name = "name")
    private String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) &&
                Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
