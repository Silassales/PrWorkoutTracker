package com.timothy.silas.prworkouttracker.Category;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Category.Helper.CategorySimpleItemTouchHelperCallback;
import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.Home.Category.HomeCategoryFragment;
import com.timothy.silas.prworkouttracker.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.appcompat.widget.Toolbar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /* allow this fragment to customize the toolbar */
        setHasOptionsMenu(true);

        final View view = inflater.inflate(R.layout.category_fragment, container, false);

        Spinner sortSpinner = view.findViewById(R.id.category_sort_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categorySortArray, R.layout.sort_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(getSortSpinnerListener(getResources().getStringArray(R.array.categorySortArray)));

        RecyclerView recyclerView = view.findViewById(R.id.category_list_view);
        RecyclerView.LayoutManager recyclerManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerManager);

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), position -> {
            Log.i("CategoryFragment", "Clicked on row: " + position);
            displayCategory(position);
        });
        RecyclerView.Adapter recyclerAdapter = categoryAdapter;
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton addFAB = view.findViewById(R.id.addCategoryActionButton);
        addFAB.setOnClickListener(view1 -> createAddCategoryDialog());

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        categoryViewModel =  ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.getCategoryList().observe(CategoryFragment.this, categories -> categoryAdapter.addItems(categories));
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.category_frag_title));

        inflater.inflate(R.menu.category, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void displayCategory(int position) {
        Category categoryToSortby = categoryViewModel.getCategoryList().getValue().get(position);
        Log.i("category", "Displaying category: " + categoryToSortby.getName());
        Log.i("category", "Number: " + categoryViewModel.getExercisesByCategory(categoryToSortby));

        /*
            The idea here is to:

            transition to a HomeFragment with a argument containing the id of the category to show

            The home fragment will then try to grab that arg:

                - if the arg is null, then we are opening the home fragment from somewhere else -> just show
                    all the exercises
                - if the arg is found try to only show the exercises with that category id -> if that then
                    shows zero exercises, leave a note for the user saying that there are no exercises in this
                    category
         */

        Bundle args = new Bundle();
        args.putInt(getString(R.string.category_id_arg_tag), categoryToSortby.getId());

        // also going to put the name so that we can display to the user what category we are displaying without having to do a
        // table lookup
        args.putString(getString(R.string.category_name_arg_tab), categoryToSortby.getName());

        HomeCategoryFragment homeCategoryFragment = new HomeCategoryFragment();
        homeCategoryFragment.setArguments(args);
        if(homeCategoryFragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, homeCategoryFragment).addToBackStack("tag").commit();
        }
    }

    private void createAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Add Category");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.category_add_dialog_content, null);
        builder.setView(view);
        final EditText nameInput = view.findViewById(R.id.addCategoryNameEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            final String name = (nameInput.getText().toString().isEmpty() ?
                    getString(R.string.add_category_name_default) :
                    nameInput.getText().toString());

            Log.i("blah", "|" + name + "|" + " : " + Integer.toString(name.length()));

            categoryViewModel.addItem(new Category(null, name));

            Snackbar.make(getView(), getString(R.string.add_category_confirm_snackbar, name), Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorOnDarkTheme));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorOnDarkTheme));
    }

    private AdapterView.OnItemSelectedListener getSortSpinnerListener(String[] sortOptions) {
        return new AdapterView.OnItemSelectedListener() {
            int check = 0;
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(++check > 1) {
                    final String selectedItem = parent.getItemAtPosition(position).toString();
                    if(selectedItem.equals(sortOptions[0])) { // Default
                        Log.i("Sort Category Selected", "Sorting by Default aka ID");
                        if(categoryViewModel.getCategoryList().getValue() != null) {
                            Collections.sort(categoryViewModel.getCategoryList().getValue(), ((o1, o2) -> Integer.compare(o1.getId(), o2.getId())));
                        }
                    } else if(selectedItem.equals(sortOptions[1])) { // Name
                        Log.i("Sort Category Selected", "Sorting by Name");
                        if(categoryViewModel.getCategoryList().getValue() != null) {
                            Collections.sort(categoryViewModel.getCategoryList().getValue(), (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase()));
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        };
    }

    @Override
    public void onPause() {
        if (categoryAdapter != null) {
            if (categoryAdapter.categoriesToRemove != null) {
                for (Category category: categoryAdapter.categoriesToRemove){
                    categoryViewModel.deleteCategory(category);
                }
            }
        }
        super.onPause();
    }
}
