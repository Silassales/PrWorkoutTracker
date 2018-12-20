package com.timothy.silas.prworkouttracker.Exercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timothy.silas.prworkouttracker.Models.Exercise;
import com.timothy.silas.prworkouttracker.R;
import com.timothy.silas.prworkouttracker.Utils.DataBaseUtils.ExerciseDBUtils;

import java.util.UUID;

public class ExerciseFragment extends Fragment {

    private UUID exerciseUUID;
    private Exercise exercise;

    public void setExerciseUUID(UUID exerciseUUID) {
        this.exerciseUUID = exerciseUUID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_page_fragment, container, false);

        if(exerciseUUID != null) {
            exercise = ExerciseDBUtils.getExerciseData(exerciseUUID);
        } else {
            Log.w("ExerciseFragment", "Exercise is null in onCreateView");
            String error_text = getResources().getString(R.string.default_report_error_message) + getResources().getString(R.string.dev_email) + ". Error is Exercise is null in onCreateView";
        }

        return view;
    }

}
