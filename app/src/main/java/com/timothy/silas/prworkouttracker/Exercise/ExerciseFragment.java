package com.timothy.silas.prworkouttracker.Exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.R;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class ExerciseFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_page_fragment, container, false);
        Bundle args = getArguments();
        Exercise exercise = (Exercise) args.getSerializable(getString(R.string.EXERCISE_TAG));
        
        TextView titleView = view.findViewById(R.id.ExerciseTitleTextView);
        TextView dataView = view.findViewById(R.id.ExerciseDataTextView);

        titleView.setText(exercise.getName());
        dataView.setText(exercise.getWeight().toString());

        return view;
    }



}
