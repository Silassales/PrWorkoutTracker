package com.timothy.silas.prworkouttracker.Home;

public interface ClickListener {
    void onPositionRowClicked(int position);

    void onPositionAddButtonClicked(int position);

    void updateWeightText(int position, String newWeight);
}
