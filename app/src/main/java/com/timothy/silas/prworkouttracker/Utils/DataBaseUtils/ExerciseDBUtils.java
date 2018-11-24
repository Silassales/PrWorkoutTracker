package com.timothy.silas.prworkouttracker.Utils.DataBaseUtils;

import com.timothy.silas.prworkouttracker.Models.Exercise;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.util.UUID;

public class ExerciseDBUtils {
    private static final ExerciseDBUtils ourInstance = new ExerciseDBUtils();

    public static ExerciseDBUtils getInstance() {
        return ourInstance;
    }

    private ExerciseDBUtils() {
    }

    public static Exercise getExerciseData(UUID exerciseUUID) {
        // grab it from the DB

        return new Exercise(exerciseUUID, "MOCKED FROM DB " + exerciseUUID, 80, WtUnit.KG);
    }
}
