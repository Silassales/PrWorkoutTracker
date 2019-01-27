package com.timothy.silas.prworkouttracker.Home;

public interface HomeClickListener {
    void onPositionRowClicked(int position);

    void onPositionAddButtonClicked(int position);

    void updateWeightText(int position, String newWeight);
}
