package com.timothy.silas.prworkouttracker.Home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseFragment;
import com.timothy.silas.prworkouttracker.Models.Exercise;
import com.timothy.silas.prworkouttracker.Models.WtUnit;
import com.timothy.silas.prworkouttracker.R;
import com.timothy.silas.prworkouttracker.Utils.ExerciseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private List<Exercise> exerciseList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.home_excercise_list);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HomeAdapter(exerciseList, new ClickListener() {
            @Override
            public void onPositionRowClicked(int position) {
                Log.i("HomeFragment", "Clicked on row: " + position);
                displayExcercise(position);
            }

            @Override
            public void onPositionAddButtonClicked(int position) {
                Log.i("HomeFragment", "Clicked on add button on row: " + position);
                ExerciseUtils.addWeight(exerciseList.get(position));
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getData();

        return view;
    }

    private void displayExcercise(int position) {
        ExerciseFragment exerciseFragment = new ExerciseFragment();
        exerciseFragment.setExcercise(exerciseList.get(position));

        if(exerciseFragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, exerciseFragment).addToBackStack( "tag" ).commit();
        }
    }

    private void getData() {
        // clear the list to make sure we are getting fresh data
        exerciseList.clear();

        exerciseList.add(new Exercise(UUID.randomUUID(), "testtesttesttesttesttesttesttesttest", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 1000.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.LB));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.LB));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        exerciseList.add(new Exercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));

        mAdapter.notifyDataSetChanged();
    }

}
