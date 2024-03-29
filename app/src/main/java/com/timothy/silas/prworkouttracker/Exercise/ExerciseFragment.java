package com.timothy.silas.prworkouttracker.Exercise;

import android.os.Bundle;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ExerciseFragment extends Fragment {

    private final AppDatabase appDatabase = AppDatabase.getAppDatabase(getContext());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exercise_page_fragment, container, false);
        Bundle args = getArguments();
        if(args == null) {
            Log.e("ExerciseFragment", getString(R.string.args_null_during_exercise_creation));
            return view;
        }
        Exercise exercise = (Exercise) args.getSerializable(getString(R.string.EXERCISE_TAG));
        if(exercise == null) {
            Log.e("ExerciseFragment", getString(R.string.exercise_null_during_exercise_creation));
            return view;
        }
        
        TextView titleView = view.findViewById(R.id.ExerciseTitleTextView);
        TextView dataView = view.findViewById(R.id.ExerciseDataTextView);

        titleView.setText(exercise.getName());
        dataView.setText(exercise.getWeight().toString());

        final Spinner categorySpinner = view.findViewById(R.id.exercise_page_category_spinner);
        List<Category> listOfCategories = getCategories();
        if(listOfCategories == null) {
            Log.e("ExerciseFragment", getString(R.string.category_null_during_exercise_creation));
            return view;
        }
        ArrayList<String> listOfCategoryNames = new ArrayList<>();
        for(Category category : listOfCategories) {
            listOfCategoryNames.add(category.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, listOfCategoryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(arrayAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            int check = 0;

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long arg3)
            {
                // TODO currently there is a bug where you can not choose to change to the category that is a the top of the list at the start
                if(++check > 1) { // set on item selected listener calls this once, so make sure this is the second time called.
                    final String selectedItem = parent.getItemAtPosition(position).toString();

                    // TODO find a better way to do all of this.......
                    final Category selectedCategory = listOfCategories.get(listOfCategoryNames.indexOf(selectedItem));
                    changeExercisesCategory(exercise, selectedCategory);

                    Snackbar.make(getView(), "Exercise: " + exercise.getName() + " now belongs to Category: " + selectedCategory.getName(), Snackbar.LENGTH_LONG).show();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        return view;
    }

    private List<Category> getCategories() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> appDatabase.categoryDao().getAllBasic()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeExercisesCategory(Exercise exercise, Category category) {
        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.exerciseDao().changeCategory(exercise.getId(), category.getId());
        });
    }





}
