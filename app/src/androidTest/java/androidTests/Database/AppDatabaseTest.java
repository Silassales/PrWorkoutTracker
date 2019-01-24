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
import java.util.List;
import java.util.UUID;

import androidx.lifecycle.MutableLiveData;
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

    private Integer uuid;
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
        exerciseDao.insert(basicExercise);

        Exercise retrievedExercise = exerciseDao.getById(uuid);

        assertThat(retrievedExercise, equalTo(basicExercise));
    }

    @Test
    public void getById_withMissingData_returnsNull() {
        Integer newUUID = UUID.randomUUID();

        Exercise exercise = exerciseDao.getById(newUUID);

        assertThat(exercise, nullValue());
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

    @Test
    public void delete_withDataInDatabase_onlyDeletesExerciseWeWantItTo() {
        Integer newUUID = UUID.randomUUID();
        Exercise exercise = ExerciseUtils.createExercise(newUUID);

        exerciseDao.insert(exercise);
        exerciseDao.insert(basicExercise);

        exerciseDao.delete(exercise);

        assertThat(exerciseDao.getById(newUUID), nullValue());
        assertThat(exerciseDao.getById(uuid), equalTo(basicExercise));

    }

    /*
     ----------- GET ALL TESTS ------------
     */

    @Test
    public void getAll_with3DataPoints_returns3DataPoints() {
        exerciseDao.insert(basicExercise);
        exerciseDao.insert(ExerciseUtils.createExercise(UUID.randomUUID()));
        exerciseDao.insert(ExerciseUtils.createExercise(UUID.randomUUID()));

        assertThat(exerciseDao.getAll().size(), equalTo(3));
    }

    @Test
    public void getAll_with0DataPoints_returnsNoData() {
        assertThat(exerciseDao.getAll().size(), equalTo(0));
    }

    @Test
    public void getAll_withData_returnsCorrectData() {
        exerciseDao.insert(basicExercise);
        Integer id2 = UUID.randomUUID();
        exerciseDao.insert(ExerciseUtils.createExercise(id2));
        Integer id3 = UUID.randomUUID();
        exerciseDao.insert(ExerciseUtils.createExercise(id3));

        MutableLiveData<List<Exercise>> retrievedList = exerciseDao.getAll();

        assertThat(retrievedList.get(0), equalTo(basicExercise));
        assertThat(retrievedList.get(1).id, equalTo(id2));
        assertThat(retrievedList.get(2).id, equalTo(id3));
    }

    /*
     ----------- GET BY NAME TESTS ------------
     */

    @Test
    public void getByName_withExistingData_getsData() {
        String name = "swole city";
        basicExercise.name = name;
        exerciseDao.insert(basicExercise);

        Exercise retrievedExercise = exerciseDao.getByName(name);

        assertThat(retrievedExercise, equalTo(basicExercise));
    }

    @Test
    public void getByName_withoutExistingData_returnsNull() {
        String name = "swole city";
        basicExercise.name = name;
        exerciseDao.insert(basicExercise);

        Exercise retrievedExercise = exerciseDao.getByName("not swole city");

        assertThat(retrievedExercise, nullValue());
    }
}