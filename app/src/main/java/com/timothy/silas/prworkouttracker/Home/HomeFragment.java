package com.timothy.silas.prworkouttracker.Home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseFragment;
import com.timothy.silas.prworkouttracker.Models.WtUnit;
import com.timothy.silas.prworkouttracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.home_excercise_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        homeAdapter = new HomeAdapter(new ArrayList<>(), new ClickListener() {
            @Override
            public void onPositionRowClicked(int position) {
                Log.i("HomeFragment", "Clicked on row: " + position);
                displayExcercise(position);
            }

            @Override
            public void onPositionAddButtonClicked(int position) {
                final Exercise exercise = homeViewModel.getExerciseList().getValue().get(position);
                //TODO change value increased to a stored pref
                homeViewModel.updateWeight(exercise, exercise.getWeight() + 2.5);
            }

            @Override
            public void updateWeightText(int position, String newWeight) {
                Double newWeightDouble = homeViewModel.getExerciseList().getValue().get(position).getWeight()
                        , oldWeightValue = homeViewModel.getExerciseList().getValue().get(position).getWeight();
                try {
                    newWeightDouble = Double.parseDouble(newWeight);
                } catch (NumberFormatException e) {
                    Log.w("HomeFragment", "non double value found when updating weight text");
                }
                // if we failed to parse the new weight, we can just set the weight to what it was previously
                Log.i("Update Weight Exercise " + position, "with value " + newWeightDouble + " was " + oldWeightValue);
                homeViewModel.updateWeight(homeViewModel.getExerciseList().getValue().get(position), newWeightDouble);
            }
        });

        mAdapter = homeAdapter;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.getExerciseList().observe(HomeFragment.this, exercises -> homeAdapter.addItems(exercises));

        FloatingActionButton addFAB = view.findViewById(R.id.addExerciseActionButton);
        addFAB.setOnClickListener(view1 -> createAddExerciseDialog());

        return view;
    }


    private void displayExcercise(int position) {
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.EXERCISE_TAG), homeViewModel.getExerciseList().getValue().get(position));
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        exerciseFragment.setArguments(args);


        if(exerciseFragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, exerciseFragment).addToBackStack( "tag" ).commit();
        }
    }


    private void createAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Add Exercise");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.exercise_add_dialog_content, null);
        builder.setView(view);
        final EditText nameInput = view.findViewById(R.id.addExerciseNameEditText);
        final EditText weightInput = view.findViewById(R.id.addExerciseWeightEditText);

        final Spinner unitSpinner = view.findViewById(R.id.addExerciseWtUnitSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.wtUnitArray, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(arrayAdapter);

        builder.setPositiveButton("Add", (dialog, which) -> homeViewModel.addItem(new Exercise(UUID.randomUUID(),
                nameInput.getText().toString(),
                Double.parseDouble(weightInput.getText().toString()),
                WtUnitConverter.toWtUnit(unitSpinner.getSelectedItem().toString()))));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
