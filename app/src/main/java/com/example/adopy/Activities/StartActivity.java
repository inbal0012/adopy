package com.example.adopy.Activities;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.adopy.Fragments.ChatsFragment;
import com.example.adopy.Fragments.SearchFragment;
import com.example.adopy.R;
import com.example.adopy.Utilities.Dialogs;
import com.example.adopy.Fragments.MyPetsFragment;
import com.example.adopy.Fragments.ProfileFragment;

import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import static com.example.adopy.Utilities.RequestCodes.REQUEST_CODE_FILTER;
import static com.example.adopy.Utilities.RequestCodes.SELECT_IMAGE_REQUEST;
import static com.example.adopy.Utilities.RequestCodes.USER_IMAGE_REQUEST;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "my_StartActivity";
    private AppBarConfiguration mAppBarConfiguration;

    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
//                    new ProfileFragment()).commit();
//            navigationView.setCheckedItem(R.id.nav_profile);
//        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Log.d(TAG, "onCreateOptionsMenu: " + currentFragment);
//        if (currentFragment instanceof SearchFragment) {
//            inflater.inflate(R.menu.menu_search, menu);
//            Log.d(TAG, "onCreateOptionsMenu: menu_search");
//        } else {
            inflater.inflate(R.menu.start, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Log.d(TAG, "searchView.onOptionsItemSelected: ");
            Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_FILTER);
            Log.d("Option", "2");

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        switch (menuItem.getItemId()) {
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().remove(currentFragment).replace(R.id.nav_host_fragment,
                        new SearchFragment(), "SearchFragment").commit();
                getSupportActionBar().setTitle(getString(R.string.title_activity_search));
                //onCreateOptionsMenu(menu);
                break;
            case R.id.nav_chats:
                getSupportFragmentManager().beginTransaction().remove(currentFragment).replace(R.id.nav_host_fragment,
                        new ChatsFragment(), "ChatsFragment").commit();
                getSupportActionBar().setTitle(getString(R.string.menu_chats));
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().remove(currentFragment).replace(R.id.nav_host_fragment,
                        new ProfileFragment(), "ProfileFragment").commit();
                getSupportActionBar().setTitle(getString(R.string.title_profile));
                break;
            case R.id.nav_my_pets:
                getSupportFragmentManager().beginTransaction().remove(currentFragment).replace(R.id.nav_host_fragment,
                        new MyPetsFragment(), "MyPetsFragment").commit();
                getSupportActionBar().setTitle(getString(R.string.title_my_pets));
                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;

        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: " + requestCode);
        if (requestCode == USER_IMAGE_REQUEST) {
            //new MyImage(this, "Users", "user").onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == SELECT_IMAGE_REQUEST) {
            new Dialogs(this).onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == REQUEST_CODE_FILTER && resultCode == RESULT_OK) {
            new SearchFragment().onActivityResult(requestCode, resultCode, data);
        }
    }
}
