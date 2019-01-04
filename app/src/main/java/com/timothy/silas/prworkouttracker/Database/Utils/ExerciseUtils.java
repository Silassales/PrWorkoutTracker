package com.timothy.silas.prworkouttracker.Database.Utils;

import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Models.WtUnit;

import java.util.UUID;

public class ExerciseUtils {

    public static Exercise createExercise(UUID id) {
        Exercise exercise = new Exercise();
        exercise.id = id;
        exercise.name = "";
        exercise.weight = 0.0;
        exercise.weightUnit = WtUnit.LB;
        return exercise;
    }
}
