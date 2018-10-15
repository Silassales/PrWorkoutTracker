package com.timothy.silas.prworkouttracker.Excercise;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timothy.silas.prworkouttracker.Models.Excercise;
import com.timothy.silas.prworkouttracker.R;

public class ExerciseFragment extends Fragment {

    private Excercise excercise;

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_page_fragment, container, false);

        TextView nameView = view.findViewById(R.id.excercise_name_textview);
        if(excercise != null) {
            nameView.setText(excercise.getName());
        } else {
            Log.w("ExerciseFragment", "Exercise is null in ")

        }

        return view;
    }

}
