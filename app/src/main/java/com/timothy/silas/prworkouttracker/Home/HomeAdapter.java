package com.timothy.silas.prworkouttracker.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        public TextView weight;
        public TextView wtUnit;
        public Button addButton;
        private WeakReference<ClickListener> listenerRef;

        public MyViewHolder(View view, ClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            name = view.findViewById(R.id.exercise_name_textview);
            weight = view.findViewById(R.id.exercise_weight_textview);
            wtUnit = view.findViewById(R.id.exercise_wt_unit_textview);
            addButton = view.findViewById(R.id.exercise_add_weight_button);

            view.setOnClickListener(this);
            name.setOnClickListener(this);
            weight.setOnClickListener(this);
            wtUnit.setOnClickListener(this);
            addButton.setOnClickListener(this);
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == addButton.getId()) {
                listenerRef.get().onPositionAddButtonClicked(getAdapterPosition());
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
