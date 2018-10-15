package com.timothy.silas.prworkouttracker.Home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Models.Excercise;
import com.timothy.silas.prworkouttracker.Models.WtUnit;
import com.timothy.silas.prworkouttracker.R;
import com.timothy.silas.prworkouttracker.Utils.ExcerciseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private List<Excercise> excerciseList = new ArrayList<>();
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
        mAdapter = new HomeAdapter(excerciseList, new ClickListener() {
            @Override
            public void onPositionRowClicked(int position) {
                Log.i("HomeFragment", "Clicked on row: " + position);
            }

            @Override
            public void onPositionAddButtonClicked(int position) {
                Log.i("HomeFragment", "Clicked on add button on row: " + position);
                ExcerciseUtils.addWeight(excerciseList.get(position));
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getData();

        return view;
    }

    private void getData() {
        excerciseList.add(new Excercise(UUID.randomUUID(), "testtesttesttesttesttesttesttesttest", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));
        excerciseList.add(new Excercise(UUID.randomUUID(), "test", 123.321, WtUnit.KG));

        mAdapter.notifyDataSetChanged();
    }

}
