package com.timothy.silas.prworkouttracker.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Database.Exercise.ExerciseDao;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseFragment;
import com.timothy.silas.prworkouttracker.Models.WtUnit;
import com.timothy.silas.prworkouttracker.R;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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
        homeAdapter = new HomeAdapter(new ArrayList<Exercise>(), new ClickListener() {
            @Override
            public void onPositionRowClicked(int position) {
                Log.i("HomeFragment", "Clicked on row: " + position);
                displayExcercise(position);
            }

            @Override
            public void onPositionAddButtonClicked(int position) {
                Log.i("HomeFragment", "Clicked on add button on row: " + position);
                //TODO ExerciseUtils.addWeight(homeViewModel.getExerciseList().getValue().get(position));
                saveData(position);
            }

            @Override
            public void updatedWeightText(int position, String newWeight) {
                //TODO
//                // we will try to check if this is a new value and try to minimize unneeded updates
//                boolean needToUpdate = true;
//                // update the list with the new value in the edit text
//                try {
//                    double tempVal = Double.valueOf(newWeight);
//                    if(Double.compare(tempVal, homeViewModel.getExerciseList().getValue().get(position).getWeight()) == 0) {
//                        needToUpdate = false;
//                    } else {
//                        homeViewModel.getExerciseList().getValue().get(position).setWeight(tempVal);
//                    }
//                } catch (NumberFormatException e) {
//                    Log.w("HomeFragment", "non double value found when updating weight text");
//                    // if we return here the value will just be set to whatever it was before this update was done
//                    return;
//                }
//                if(needToUpdate) saveData(position);
            }
        });
        mAdapter = homeAdapter;
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        homeViewModel.getExerciseList().observe(HomeFragment.this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                homeAdapter.addItems(exercises);
            }
        });

        homeViewModel.addItem(new Exercise(UUID.randomUUID(), "blah", 0.0, WtUnit.LB));
        homeViewModel.addItem(new Exercise(UUID.randomUUID(), "blah2", 0.2, WtUnit.KG));

        return view;
    }

    private void displayExcercise(int position) {
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        exerciseFragment.setExerciseUUID(homeViewModel.getExerciseList().getValue().get(position).getId());

        if(exerciseFragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, exerciseFragment).addToBackStack( "tag" ).commit();
        }
    }

    private void saveData(int position) {
        // TODO: save to db
        mAdapter.notifyDataSetChanged();
    }
}
