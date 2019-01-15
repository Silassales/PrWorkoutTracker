package com.timothy.silas.prworkouttracker.Home;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeItemTouchHelperAdapter;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeItemTouchHelperViewHolder;
import com.timothy.silas.prworkouttracker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements HomeItemTouchHelperAdapter {

    private final ClickListener listener;
    private List<Exercise> exerciseList;
    public List<Exercise> exercisesToRemove;

    public HomeAdapter(List<Exercise> exerciseList, ClickListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
        this.exercisesToRemove = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exersice_list_row, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.name.setText(exercise.getName());
        holder.weight.setText(exercise.getWeight().toString());
        holder.wtUnit.setText(exercise.getWeightUnit().toString());
    }

    public void addItems(List<Exercise> exercises) {
        Log.i("Home Adapter", "Added " + (exercises.size() - this.exerciseList.size()) + " new exercises from the db");
        this.exerciseList = exercises;
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Exercise exerciseToRemove = exerciseList.get(adapterPosition);

        Snackbar snackbar = Snackbar.make(recyclerView, "Exercise \"" + exerciseToRemove.getName() + "\" removed!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", view -> {
                    exerciseList.add(adapterPosition, exerciseToRemove);
                    notifyItemInserted(adapterPosition);
                    recyclerView.scrollToPosition(adapterPosition);
                    exercisesToRemove.remove(exerciseToRemove);
                });
        snackbar.show();
        exerciseList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        exercisesToRemove.add(exerciseToRemove);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, HomeItemTouchHelperViewHolder {
        public TextView name;
        public EditText weight;
        public TextView wtUnit;
        public Button addButton;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View view, ClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            name = view.findViewById(R.id.exercise_name_textview);
            weight = view.findViewById(R.id.exercise_weight_edittext);
            wtUnit = view.findViewById(R.id.exercise_wt_unit_textview);
            addButton = view.findViewById(R.id.exercise_add_weight_button);

            view.setOnClickListener(this);
            name.setOnClickListener(this);
            weight.setOnClickListener(this);
            wtUnit.setOnClickListener(this);
            addButton.setOnClickListener(this);

            weight.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    listenerRef.get().updateWeightText(getAdapterPosition(), weight.getText().toString());
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    return true;
                }
                return false;
            });
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == addButton.getId()) {
                listenerRef.get().onPositionAddButtonClicked(getAdapterPosition());
            } else if (v.getId() == weight.getId()) {
                // do nothing we want to edit in this case
            } else {
                listenerRef.get().onPositionRowClicked(getAdapterPosition());
            }
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
        return exerciseList.size();
    }
}
