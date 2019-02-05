package com.timothy.silas.prworkouttracker.Category;

import android.app.Application;
import android.os.AsyncTask;

import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Category.Category;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CategoryViewModel extends AndroidViewModel {

    private final LiveData<List<Category>> categoryList;

    private AppDatabase appDatabase;

    public CategoryViewModel(Application application) {
        super(application);

        appDatabase = AppDatabase.getAppDatabase(this.getApplication());

        categoryList = appDatabase.categoryDao().getAll();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public List<Exercise> getExercisesByCategory(Category category) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> appDatabase.exerciseDao().getByCategory(category.getId())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCategory(Category category) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.categoryDao().delete(category);
        });
    }

    public void addItem(Category category) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            appDatabase.categoryDao().insert(category);
        });
    }
}
