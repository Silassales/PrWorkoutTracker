package com.timothy.silas.prworkouttracker.Exercise;

import android.util.Log;

import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;


public class ExerciseUtils {

    public static void addWeight(Exercise exercise) {
        double amountToAdd = 0;

        switch (exercise.getWeightUnit()){
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

        Log.i("ExcerciseUtil#AddWeight", String.format("Exercise %s is in units %s, adding %s %s's.", exercise.getName(), exercise.getWeightUnit(), amountToAdd, exercise.getWeightUnit()));
        //exercise.setWeight(exercise.getWeight() + amountToAdd);
    }
}