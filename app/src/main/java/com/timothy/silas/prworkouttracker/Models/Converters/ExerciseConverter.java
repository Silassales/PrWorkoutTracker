package com.timothy.silas.prworkouttracker.Models.Converters;

import com.timothy.silas.prworkouttracker.Models.Exercise;

public class ExerciseConverter {

    public static com.timothy.silas.prworkouttracker.Database.Exercise.Exercise toDBExercise(Exercise exercise) {
        com.timothy.silas.prworkouttracker.Database.Exercise.Exercise newExercise = new com.timothy.silas.prworkouttracker.Database.Exercise.Exercise();
        newExercise.name = exercise.getName();
        newExercise.id = exercise.getId();
        newExercise.weightUnit = exercise.getWtUnit();
        newExercise.weight = exercise.getWeight();
        return newExercise;
    }

    public static Exercise fromDBExercise(com.timothy.silas.prworkouttracker.Database.Exercise.Exercise exercise) {
        Exercise newExercise = new Exercise(exercise.id, exercise.name, exercise.weight, exercise.weightUnit);
        return newExercise;
    }
}
