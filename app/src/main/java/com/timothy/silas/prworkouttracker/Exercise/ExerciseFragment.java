package com.timothy.silas.prworkouttracker.Exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.R;

import java.util.UUID;

import androidx.fragment.app.Fragment;

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
            //exercise = ExerciseDBUtils.getExerciseData(exerciseUUID);
        } else {
            Log.w("ExerciseFragment", "Exercise is null in onCreateView");
            String error_text = getResources().getString(R.string.default_report_error_message) + getResources().getString(R.string.dev_email) + ". Error is Exercise is null in onCreateView";
        }

        return view;
    }

}
