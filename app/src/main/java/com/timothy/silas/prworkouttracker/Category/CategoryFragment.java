package com.timothy.silas.prworkouttracker.Category;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Category.Helper.CategorySimpleItemTouchHelperCallback;
import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Database.Utils.WtUnitConverter;
import com.timothy.silas.prworkouttracker.Exercise.ExerciseFragment;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeSimpleItemTouchHelperCallback;
import com.timothy.silas.prworkouttracker.Home.HomeAdapter;
import com.timothy.silas.prworkouttracker.Home.HomeFragment;
import com.timothy.silas.prworkouttracker.Home.HomeViewModel;
import com.timothy.silas.prworkouttracker.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFragment extends Fragment {

    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    private Spinner sortSpinner;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerManager;

    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.category_fragment, container, false);

        sortSpinner = view.findViewById(R.id.category_sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categorySortArray, R.layout.sort_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(getSortSpinnerListener(getResources().getStringArray(R.array.categorySortArray)));

        recyclerView = view.findViewById(R.id.category_list_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerManager);

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), new ClickListener() {
            @Override
            public void onPositionRowClicked(int position) {
                Log.i("CategoryFragment", "Clicked on row: " + position);
                displayCategory(position);
            }
        });
        recyclerAdapter = categoryAdapter;
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton addFAB = view.findViewById(R.id.addCategoryActionButton);
        addFAB.setOnClickListener(view1 -> createAddExerciseDialog());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        ItemTouchHelper.Callback callback = new CategorySimpleItemTouchHelperCallback(categoryAdapter, recyclerView);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        categoryViewModel =  ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryList().observe(CategoryFragment.this, categories -> categoryAdapter.addItems(categories));
        return view;
    }

    private void displayCategory(int position) {
        // TODO
        Log.i("category", "Displaying category: " + categoryViewModel.getCategoryList().getValue().get(position).getName());
    }

    private void createAddExerciseDialog() {
        // TODO
        Log.i("category", "adding exercise");
    }

    private AdapterView.OnItemSelectedListener getSortSpinnerListener(String[] sortOptions) {
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals(sortOptions[0])) { // Default
                    Log.i("Sort Exercise Selected", "Sorting by Default aka ID");
                    Collections.sort(categoryViewModel.getExerciseList().getValue(), ((o1, o2) -> Integer.compare(o1.getId(), o2.getId())));
                } else if(selectedItem.equals(sortOptions[1])) { // Name
                    Log.i("Sort Exercise Selected", "Sorting by Name");
                    Collections.sort(categoryViewModel.getExerciseList().getValue(), (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
                }
                homeAdapter.notifyDataSetChanged();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        };
    }
}
