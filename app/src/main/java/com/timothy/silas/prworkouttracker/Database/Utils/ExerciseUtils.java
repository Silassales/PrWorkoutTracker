package com.timothy.silas.prworkouttracker.Database.Utils;

import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

public class ExerciseUtils {

    public static Exercise createExercise(Integer id) {
        Exercise exercise = new Exercise(id, "", 0.0, WtUnit.LB);
        return exercise;
    }
}
