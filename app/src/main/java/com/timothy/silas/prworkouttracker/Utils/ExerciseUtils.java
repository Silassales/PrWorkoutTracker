package com.timothy.silas.prworkouttracker.Utils;

import android.util.Log;

import com.timothy.silas.prworkouttracker.Models.Excercise;

public class ExcerciseUtils {

    public static void addWeight(Excercise excercise) {
        double amountToAdd = 0;

        switch (excercise.getWtUnit()){
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

        Log.i("ExcerciseUtil#AddWeight", String.format("Excercise %s is in units %s, adding %s %s's.", excercise.getName(), excercise.getWtUnit(), amountToAdd, excercise.getWtUnit()));
        excercise.setWeight(excercise.getWeight() + amountToAdd);
    }
}