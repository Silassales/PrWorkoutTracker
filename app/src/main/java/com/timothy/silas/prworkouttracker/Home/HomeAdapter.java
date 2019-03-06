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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeItemTouchHelperAdapter;
import com.timothy.silas.prworkouttracker.Home.Helper.HomeItemTouchHelperViewHolder;
import com.timothy.silas.prworkouttracker.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements HomeItemTouchHelperAdapter {

    private final HomeClickListener listener;
    private List<Exercise> exerciseList;
    public List<Exercise> exercisesToRemove;
    private Integer categoryToSortBy;

    public HomeAdapter(List<Exercise> exerciseList, HomeClickListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
        this.exercisesToRemove = new ArrayList<>();
        this.categoryToSortBy = R.integer.no_category_found_defualt_val;
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

        // TODO make these images nicer and much much smaller -> look into background threads if scrolling is still
        // TODO laggy after making the images smaller -> or bitmaps or so on
        // TODO cut this stuff into smaller methods
        /* set the barbell preview based on the exercise weight */
        // first do a nice clear in case there is already weight on there:
        holder.slot1.setImageResource(R.drawable.w_empty);
        holder.slot2.setImageResource(R.drawable.w_empty);
        holder.slot3.setImageResource(R.drawable.w_empty);
        holder.slot4.setImageResource(R.drawable.w_empty);
        holder.slot5.setImageResource(R.drawable.w_empty);

        Double weight = exercise.getWeight();
        weight -= 20; // for the bar
        weight /= 2; // we just show them half the bar
        /*
            For example a 60kg bench ->

            60 - 20 for the bar = 40
            40 / 2 = 20 -> we show one 20kg plate on the display
         */

        // TODO do this for pounds as well
        int resourceIdToSet;
        int currentSlot = 1;

        Log.i("testing", "starting setting loop with weight: " + weight);
        while(weight > 0) {
            if(weight >= 20) {
                resourceIdToSet = R.drawable.w_20kg_blue;
                weight -= 20;
            } else if (weight >= 10) {
                resourceIdToSet = R.drawable.w_10kg_green;
                weight -= 10;
            } else if(weight >= 5) {
                resourceIdToSet = R.drawable.w_5kg_yellow;
                weight -= 5;
            } else if(weight >= 2.5) {
                resourceIdToSet = R.drawable.w_2_5kg;
                weight -= 2.5;
            } else if(weight >= 1.25) {
                resourceIdToSet = R.drawable.w_1_25kg;
                weight -= 1.25;
            } else {
                break; // TODO display some additional weight needed message to the user
            }

            Log.i("testing", "decided that we needed id " + resourceIdToSet + " and we now have left over weight: " + weight);

            switch (currentSlot) {
                case 1 :
                    holder.slot1.setImageResource(resourceIdToSet);
                    break;
                case 2:
                    holder.slot2.setImageResource(resourceIdToSet);
                    break;
                case 3:
                    holder.slot3.setImageResource(resourceIdToSet);
                    break;
                case 4:
                    holder.slot4.setImageResource(resourceIdToSet);
                    break;
                case 5:
                    holder.slot5.setImageResource(resourceIdToSet);
                    break;
                default:
                    // set to a negative number so that we kick out of the loop
                    weight = -1.0; // TODO display some info about additional weight being needed
                    break;
            }
            currentSlot++;
        }
    }

    public void setCategoryToSortBy(Integer categoryToSortBy) {
        this.categoryToSortBy = categoryToSortBy;
    }

    public void addItems(List<Exercise> exercises) {
        Log.i("Home Adapter", "Added " + (exercises.size() - this.exerciseList.size()) + " new exercises from the db");
        // see the comment in CategoryFragment#displayCategory for context
        if(this.categoryToSortBy != R.integer.no_category_found_defualt_val) {
            Exercise[] exercisesToSort = exercises.toArray(new Exercise[exercises.size()]); // to avoid concurrentModificationException
            for(Exercise exercise : exercisesToSort) {
                if(exercise.getCategoryId() != this.categoryToSortBy) {
                    exercises.remove(exercise);
                }
            }
        }
        this.exerciseList = exercises;
        notifyDataSetChanged();
    }

    @Override
    public void onItemRemove(final RecyclerView.ViewHolder viewHolder, final RecyclerView recyclerView) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Exercise exerciseToRemove = exerciseList.get(adapterPosition);

        // TODO confirm that we want the length for the remove snackbar to be infinite

        Snackbar snackbar = Snackbar.make(recyclerView, "Exercise \"" + exerciseToRemove.getName() + "\" removed!", Snackbar.LENGTH_INDEFINITE)
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

        public ImageView slot1;
        public ImageView slot2;
        public ImageView slot3;
        public ImageView slot4;
        public ImageView slot5;

        private WeakReference<HomeClickListener> listenerRef;

        public MyViewHolder(View view, HomeClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            name = view.findViewById(R.id.exercise_name_textview);
            weight = view.findViewById(R.id.exercise_weight_edittext);
            wtUnit = view.findViewById(R.id.exercise_wt_unit_textview);
            addButton = view.findViewById(R.id.exercise_add_weight_button);

            slot1 = view.findViewById(R.id.barbell_slot1_iv);
            slot2 = view.findViewById(R.id.barbell_slot2_iv);
            slot3 = view.findViewById(R.id.barbell_slot3_iv);
            slot4 = view.findViewById(R.id.barbell_slot4_iv);
            slot5 = view.findViewById(R.id.barbell_slot5_iv);

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
