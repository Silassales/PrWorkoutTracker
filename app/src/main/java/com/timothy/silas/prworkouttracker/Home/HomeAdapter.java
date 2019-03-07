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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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

        // TODO cut this stuff into smaller methods
        /* set the barbell preview based on the exercise weight */

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
        int previousSlotId;
        int slotId = 0;
        boolean firstSlotEmpty = true;

        ConstraintLayout constraintLayout = holder.itemView.findViewById(R.id.exercise_list_constraintLayout);
        ImageView barbellImageView = holder.itemView.findViewById(R.id.barbell_iv);

        // first check to see if we have any weights already on, and if so take them offfff (clean up your weights people jeez)
        if(!holder.slotIds.isEmpty()) {
            for(int i : holder.slotIds) {
                constraintLayout.removeView(holder.itemView.findViewById(i));
            }
        }

        while(weight > 0) {
            // first determine the icon we need to use
            if(weight >= 20) {
                resourceIdToSet = R.drawable.ic_20kg;
                weight -= 20;
            } else if (weight >= 10) {
                resourceIdToSet = R.drawable.ic_20kg;
                weight -= 10;
            } else if(weight >= 5) {
                resourceIdToSet = R.drawable.ic_20kg;
                weight -= 5;
            } else if(weight >= 2.5) {
                resourceIdToSet = R.drawable.ic_20kg;
                weight -= 2.5;
            } else if(weight >= 1.25) {
                resourceIdToSet = R.drawable.ic_20kg;
                weight -= 1.25;
            } else {
                break; // TODO display some additional weight needed message to the user
            }

            // next generate a new image view for us to display
            ImageView imageView = new ImageView(holder.itemView.getContext());

            // generate and store a new slot id so that if we need to display another weight to the right
            // we can set up the constraints correctly, also add all slot ids to an arraylist so that we
            // can erase all previous images on a weight change
            previousSlotId = slotId;
            slotId = View.generateViewId(); // named as such because we use as such down below
            holder.slotIds.add(slotId);
            imageView.setId(slotId);

            // setup our layout width and height params
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Math.round(holder.itemView.getContext().getResources().getDimension(R.dimen.barbell_image_height))));

            imageView.setImageResource(resourceIdToSet);
            constraintLayout.addView(imageView);

            // time for the fun constraints..
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);

            // connect our new image view to the top and bottom of the barbell
            constraintSet.connect(imageView.getId(), ConstraintSet.BOTTOM, barbellImageView.getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(imageView.getId(), ConstraintSet.TOP, barbellImageView.getId(), ConstraintSet.TOP);

            // if the first slot is empty we need a larger margin, and to connect to the barbell not the plate in front
            if(firstSlotEmpty) {
                int margin = Math.round(holder.itemView.getContext().getResources().getDimension(R.dimen.barbell_first_slot_margin));
                constraintSet.connect(imageView.getId(), ConstraintSet.END, barbellImageView.getId(), ConstraintSet.END, margin);
                firstSlotEmpty = false;
            } else {
                int margin = Math.round(holder.itemView.getContext().getResources().getDimension(R.dimen.barbell_slot_margin));
                constraintSet.connect(imageView.getId(), ConstraintSet.END, previousSlotId, ConstraintSet.START, margin);
            }

            // apply and pray to the lord that everything is connected properly
            constraintSet.applyTo(constraintLayout);
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

        public ArrayList<Integer> slotIds = new ArrayList<>();

        private WeakReference<HomeClickListener> listenerRef;

        public MyViewHolder(View view, HomeClickListener listener) {
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
