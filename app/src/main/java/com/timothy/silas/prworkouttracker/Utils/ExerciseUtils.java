package com.timothy.silas.prworkouttracker.Utils;

import android.util.Log;

import com.timothy.silas.prworkouttracker.Models.Exercise;

public class ExerciseUtils {

    public static void addWeight(Exercise exercise) {
        double amountToAdd = 0;

        switch (exercise.getWtUnit()){
            case KG:
                amountToAdd = 2.5;
                break;
            case LB:
                amountToAdd = 5;
                break;
            default:
                amountToAdd = 5;
                break;
        }

        Log.i("ExcerciseUtil#AddWeight", String.format("Exercise %s is in units %s, adding %s %s's.", exercise.getName(), exercise.getWtUnit(), amountToAdd, exercise.getWtUnit()));
        exercise.setWeight(exercise.getWeight() + amountToAdd);
    }
}