package com.timothy.silas.prworkouttracker.Home.Category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Home.HomeAdapter;
import com.timothy.silas.prworkouttracker.Home.HomeClickListener;
import com.timothy.silas.prworkouttracker.Home.HomeFragment;
import com.timothy.silas.prworkouttracker.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HomeCategoryFragment extends HomeFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_category_fragment, container, false);

        super.setUpStandardView(view);

        String categoryName = getString(R.string.exercise_category_label_default_text);
        Bundle args = getArguments();
        if(args != null) {
            categoryName = args.getString(getString(R.string.category_name_arg_tab), getString(R.string.exercise_category_label_default_text));
        }

        TextView categoryLabelTextView = view.findViewById(R.id.exercise_category_label_text_view);
        categoryLabelTextView.setText(getString(R.string.exercise_category_label_prefix) + " " + categoryName);

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
}
