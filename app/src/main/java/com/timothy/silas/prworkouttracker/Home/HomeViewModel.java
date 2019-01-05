package com.timothy.silas.prworkouttracker.Home;

import android.app.Application;
import android.os.AsyncTask;

import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;

import java.util.List;
import java.util.prefs.AbstractPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class HomeViewModel extends AndroidViewModel {

    private final LiveData<List<Exercise>> exerciseList;

    private AppDatabase appDatabase;

    public HomeViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

        exerciseList = appDatabase.exerciseDao().getAll();
    }

    public LiveData<List<Exercise>> getExerciseList() {
        return exerciseList;
    }

    public void addItem(Exercise exercise) {
        new insertAsyncTask(appDatabase).execute(exercise);
    }

    private static class insertAsyncTask extends AsyncTask<Exercise, Void, Void> {
        private AppDatabase db;

        insertAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }

        @Override
        protected Void doInBackground(final Exercise... params) {
            db.exerciseDao().insert(params[0]);
            return null;
        }
    }

}
