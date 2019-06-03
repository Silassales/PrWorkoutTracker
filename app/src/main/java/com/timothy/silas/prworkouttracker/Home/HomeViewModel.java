package com.timothy.silas.prworkouttracker.Home;

import android.app.Application;
import android.os.AsyncTask;

import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public LiveData<List<Exercise>> getExerciseList() { return exerciseList;
    }

    public void updateWeight(Exercise exercise, Double newWeight) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.exerciseDao().updateWeight(exercise.getId(), newWeight);
        });
    }

    public void deleteExercise(Exercise exercise) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.exerciseDao().delete(exercise);
        });
    }

    public void deleteAllExercises() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.exerciseDao().nukeTable();
        });
    }

    public void deleteAllCategories() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.categoryDao().nukeTable();
        });
    }

    public void addItem(Exercise exercise) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.exerciseDao().insert(exercise);
        });
    }

    public void addCategory(Category category) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.categoryDao().insert(category);
        });
    }

    public Category getCategoryByName(String categoryName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> appDatabase.categoryDao().getByName(categoryName)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> getCategories() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> appDatabase.categoryDao().getAllBasic()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


}
