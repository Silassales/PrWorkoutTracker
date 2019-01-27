package com.timothy.silas.prworkouttracker.Category;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Category.Helper.CategoryItemTouchHelperAdapter;
import com.timothy.silas.prworkouttracker.Category.Helper.CategoryItemTouchHelperViewHolder;
import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements CategoryItemTouchHelperAdapter {

    private final CategoryClickListener listener;
    private List<Category> categoryList;
    public List<Category> categoriesToRemove;

    CategoryAdapter(List<Category> categoryList, CategoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
        this.categoriesToRemove = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_row, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.name.setText(category.getName());
    }

    void addItems(List<Category> categories) {
        Log.i("Category Adapter", "Added " + (categories.size() - this.categoryList.size()) + " new categories from the db");
        this.categoryList = categories;
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Category categoryToRemove = categoryList.get(adapterPosition);

        Snackbar snackbar = Snackbar.make(recyclerView, "Category \"" + categoryToRemove.getName() + "\" removed!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> {
                    categoryList.add(adapterPosition, categoryToRemove);
                    notifyItemInserted(adapterPosition);
                    recyclerView.scrollToPosition(adapterPosition);
                    categoriesToRemove.remove(categoryToRemove);
                });
        snackbar.show();
        categoryList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        categoriesToRemove.add(categoryToRemove);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CategoryItemTouchHelperViewHolder {
        public TextView name;

        private WeakReference<CategoryClickListener> listenerRef;

        MyViewHolder(View view, CategoryClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            name = view.findViewById(R.id.category_name_textview);

            view.setOnClickListener(this);
            name.setOnClickListener(this);
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionRowClicked(getAdapterPosition());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
