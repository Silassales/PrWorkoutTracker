package com.timothy.silas.prworkouttracker.Database.Utils;

import android.util.Log;

import com.timothy.silas.prworkouttracker.Models.WtUnit;

import androidx.room.TypeConverter;

public class WtUnitConverter {

    @TypeConverter
    public static WtUnit toWtUnit (String wtUnitString) {
        if(wtUnitString == null) return null;

        if(wtUnitString.equals("LB")) {
            return WtUnit.LB;
        } else if (wtUnitString.equals("KG")) {
            return WtUnit.KG;
        } else {
            Log.w("WtUnitConversion","Invalid wt Unit string in conversion");
            return WtUnit.KG;
        }
    }

    @TypeConverter
    public static String toString(WtUnit wtUnit) {
        return wtUnit == null ? null : wtUnit.toString();
    }

}
