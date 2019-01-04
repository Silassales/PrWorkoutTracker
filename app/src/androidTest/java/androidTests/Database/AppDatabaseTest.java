package androidTests.Database;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.timothy.silas.prworkouttracker.Database.AppDatabase;
import com.timothy.silas.prworkouttracker.Database.Exercise.Exercise;
import com.timothy.silas.prworkouttracker.Database.Exercise.ExerciseDao;
import com.timothy.silas.prworkouttracker.Database.Utils.ExerciseUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.UUID;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;


@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private ExerciseDao exerciseDao;
    private AppDatabase appDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        exerciseDao = appDatabase.exerciseDao();
    }

    @After
    public void closeDb() throws IOException {
        appDatabase.close();
    }

    /*
        These tests should be done in order of their dependencies (insert and select before delete)
     */

    /*
      ----------- INSERT TESTS ------------
     */
    @Test
    public void insert_withGoodData_insertsCorrectly() throws Exception {
        UUID id = UUID.randomUUID();
        Exercise exercise = ExerciseUtils.createExercise(id);
        exercise.name = "testName";
        exerciseDao.insert(exercise);

        Exercise testExercise = exerciseDao.getById(id);
        assertThat(testExercise, equalTo(exercise));
    }

    @Test(expected = NullPointerException.class)
    public void insert_withNullData_throwsNullException() {
        Exercise exercise = null;

        exerciseDao.insert(exercise);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void insert_withNullIdData_throwsSQLException() {
        Exercise exercise = ExerciseUtils.createExercise(UUID.randomUUID());
        exercise.id = null;

        exerciseDao.insert(exercise);
    }

    /*
     ----------- GET BY ID TESTS ------------
     */



    /*
     ----------- DELETE TESTS ------------
     */

    @Test
    public void delete_withDataInDatabase_deletesData() {
        UUID uuid = UUID.randomUUID();
        Exercise exercise = ExerciseUtils.createExercise(uuid);
        exerciseDao.insert(exercise);

        exerciseDao.delete(exercise);

        assertThat(exerciseDao.getById(uuid), nullValue());
    }

}