package com.timothy.silas.prworkouttracker.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseFragment;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeSimpleItemTouchHelperCallback;
import com.timothy.silas.prworkouttracker.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeAdapter homeAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Spinner sortSpinner;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        sortSpinner = view.findViewById(R.id.exercise_sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.exerciseSortArray, R.layout.sort_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(getSortSpinnerListener(getResources().getStringArray(R.array.exerciseSortArray)));

        mRecyclerView = view.findViewById(R.id.home_excercise_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        homeAdapter = new HomeAdapter(new ArrayList<>(), new HomeClickListener() {
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

        FloatingActionButton addFAB = view.findViewById(R.id.addExerciseActionButton);
        addFAB.setOnClickListener(view1 -> createAddExerciseDialog());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && addFAB.getVisibility() == View.VISIBLE) {
                    addFAB.hide();
                } else if (dy < 0 && addFAB.getVisibility() != View.VISIBLE) {
                    addFAB.show();
                }
            }
        });

        ItemTouchHelper.Callback callback = new HomeSimpleItemTouchHelperCallback(homeAdapter, mRecyclerView);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getExerciseList().observe(HomeFragment.this, exercises -> homeAdapter.addItems(exercises));

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // see the comment in CategoryFragment#displayCategory for context
        Bundle args = getArguments();
        if(args != null) {
            final int categoryToSortBy = args.getInt(getString(R.string.category_id_arg_tag), R.integer.no_category_found_defualt_val);
            if(categoryToSortBy != R.integer.no_category_found_defualt_val) {
                homeAdapter.setCategoryToSortBy(categoryToSortBy);
            }
        }
    }


    @Override
    public void onPause() {
        if (homeAdapter != null) {
            if (homeAdapter.exercisesToRemove != null) {
                for (Exercise exercise: homeAdapter.exercisesToRemove){
                    homeViewModel.deleteExercise(exercise);
                }
            }
        }
        super.onPause();
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

        builder.setPositiveButton("Add", (dialog, which) -> {
            final String name = (nameInput.getText().toString().isEmpty() ?
                    getString(R.string.add_exercise_name_default) :
                    nameInput.getText().toString());
            final Double weight = (weightInput.getText().toString().isEmpty() ?
                    Double.parseDouble(getString(R.string.add_exercise_weight_default)) :
                    Double.parseDouble(weightInput.getText().toString()));

            homeViewModel.addItem(new Exercise(null,
                    name,
                    weight,
                    WtUnitConverter.toWtUnit(unitSpinner.getSelectedItem().toString()),
                    null));

            Snackbar.make(getView(), getString(R.string.add_exercise_confirm_snackbar, name), Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private AdapterView.OnItemSelectedListener getSortSpinnerListener(String[] sortOptions) {
        return new AdapterView.OnItemSelectedListener() {

            int check = 0;

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // TODO add categories / workouts as a option for sorting
                    if(++check > 1) {
                        if(homeViewModel.getExerciseList().getValue() == null) {
                            // TODO notify the user
                            return;
                        }

                        final String selectedItem = parent.getItemAtPosition(position).toString();
                        if(selectedItem.equals(sortOptions[0])) { // Default
                            Log.i("Sort Exercise Selected", "Sorting by Default aka ID");
                            Collections.sort(homeViewModel.getExerciseList().getValue(), ((o1, o2) -> Integer.compare(o1.getId(), o2.getId())));
                        } else if(selectedItem.equals(sortOptions[1])) { // Name
                            Log.i("Sort Exercise Selected", "Sorting by Name");
                            Collections.sort(homeViewModel.getExerciseList().getValue(), (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
                        } else if(selectedItem.equals(sortOptions[2])) { // Weight
                            Log.i("Sort Exercise Selected", "Sorting by Weight");
                            Collections.sort(homeViewModel.getExerciseList().getValue(), (o1, o2) -> Double.compare(o2.getWeight(), o1.getWeight()));
                        } else if(selectedItem.equals(sortOptions[3])) { // Weight Unit
                            Log.i("Sort Exercise Selected", "Sorting by Weight Unit");
                            Collections.sort(homeViewModel.getExerciseList().getValue(), (o1, o2) -> o1.getWeightUnit().toString().compareTo(o2.getWeightUnit().toString()));
                        }
                        homeAdapter.notifyDataSetChanged();
                    }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        };
    }

}
