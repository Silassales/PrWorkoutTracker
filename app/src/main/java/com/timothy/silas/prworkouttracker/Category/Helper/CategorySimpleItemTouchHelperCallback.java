package com.timothy.silas.prworkouttracker.Category.Helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CategorySimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final CategoryItemTouchHelperAdapter categoryItemTouchHelperAdapter;
    private final RecyclerView recyclerView;

    public CategorySimpleItemTouchHelperCallback(CategoryItemTouchHelperAdapter adapter, RecyclerView recyclerView) {
        this.categoryItemTouchHelperAdapter = adapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        categoryItemTouchHelperAdapter.onItemRemove(viewHolder, recyclerView);
    }
}

