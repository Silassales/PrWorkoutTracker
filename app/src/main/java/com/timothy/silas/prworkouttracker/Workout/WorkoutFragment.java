package com.timothy.silas.prworkouttracker.Workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timothy.silas.prworkouttracker.R;

import androidx.fragment.app.Fragment;

public class WorkoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.workout_fragment, container, false);

        return view;
    }
}
