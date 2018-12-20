package com.timothy.silas.prworkouttracker.Home;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timothy.silas.prworkouttracker.ClickListener;
import com.timothy.silas.prworkouttracker.Models.Exercise;
import com.timothy.silas.prworkouttracker.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private final ClickListener listener;
    private List<Exercise> exerciseList;

    public HomeAdapter(List<Exercise> exerciseList, ClickListener listener) {
        this.exerciseList = exerciseList;
        this.listener = listener;
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
        holder.weight.setText(exercise.getWeightFormatted());
        holder.wtUnit.setText(exercise.getWtUnit().toString());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            weight.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        listenerRef.get().updatedWeightText(getAdapterPosition(), weight.getText().toString());
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        v.clearFocus();
                        return true;
                    }
                    return false;
                }
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
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
