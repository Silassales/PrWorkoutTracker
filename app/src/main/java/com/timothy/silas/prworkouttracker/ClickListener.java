package com.timothy.silas.prworkouttracker;

public interface ClickListener {
    void onPositionRowClicked(int position);

    void onPositionAddButtonClicked(int position);

    void updatedWeightText(int position, String newWeight);
}
