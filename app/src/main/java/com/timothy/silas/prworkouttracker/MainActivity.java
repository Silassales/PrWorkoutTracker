package com.timothy.silas.prworkouttracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.timothy.silas.prworkouttracker.Calendar.CalendarFragment;
import com.timothy.silas.prworkouttracker.Category.CategoryFragment;
import com.timothy.silas.prworkouttracker.Home.HomeFragment;
import com.timothy.silas.prworkouttracker.Workout.WorkoutFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /*
        TODO alex's suggestions:
        click on the exercises -> show description, cues, and tips
        Add number of reps -> show it as ex 5 RM
        add a date changed -> when you did this
        change the current page highlight so that text is easier to see
     */

    ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        displayView(R.id.nav_home, true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId(), false);
        return true;
    }

    public void displayView(int viewId, boolean firstTime) {

        Fragment fragment = null;

        switch (viewId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_categories:
                fragment = new CategoryFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(firstTime) {
                ft.replace(R.id.content_frame, fragment).commit();
            } else {
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
    }

}
