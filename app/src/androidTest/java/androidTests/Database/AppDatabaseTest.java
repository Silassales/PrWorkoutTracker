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

    private UUID uuid;
    private Exercise basicExercise;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        exerciseDao = appDatabase.exerciseDao();

        uuid = UUID.randomUUID();
        basicExercise = ExerciseUtils.createExercise(uuid);
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
        exerciseDao.insert(basicExercise);

        Exercise testExercise = exerciseDao.getById(uuid);
        assertThat(testExercise, equalTo(basicExercise));
    }

    @Test(expected = NullPointerException.class)
    public void insert_withNullData_throwsNullException() {
        Exercise exercise = null;

        exerciseDao.insert(exercise);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void insert_withNullIdData_throwsSQLException() {
        basicExercise.id = null;

        exerciseDao.insert(basicExercise);
    }

    /*
     ----------- GET BY ID TESTS ------------
     */

    @Test
    public void getById_withExistingData_getsData() {

    }

    /*
     ----------- DELETE TESTS ------------
     */

    @Test
    public void delete_withDataInDatabase_deletesData() {
        exerciseDao.insert(basicExercise);

        exerciseDao.delete(basicExercise);

        assertThat(exerciseDao.getById(uuid), nullValue());
    }

}