package com.example.placementapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.placementapp.PlacementDashboardFragment;
import com.example.placementapp.R;
import com.example.placementapp.admin.fragments.SendNotificationFragment;
import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.admin.fragments.ViewStudentsProfile;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.student.UpdateProfile;
import com.example.placementapp.student.ViewYourApplicationsList;
import com.example.placementapp.student.PrepMaterialFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_navigation_drawer);

        // 6 - Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();

        new DashboardTaskRunner().execute();
    }

    private class DashboardTaskRunner extends AsyncTask<String, String, Constants.NavigationView> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Constants.NavigationView navigationViewEnum) {
            switch (navigationViewEnum) {
                case ADMIN:
                    navigationView.getMenu().removeGroup(R.id.student_group);
                    startTransactionFragment(new ViewNotificationList());
                    break;

                case STUDENT:
                    navigationView.getMenu().removeGroup(R.id.admin_group);
                    startTransactionFragment(new PlacementDashboardFragment());
                    break;
            }
        }

        @Override
        protected Constants.NavigationView doInBackground(String... strings) {
            String userType = SharedPrefHelper.getEntryfromSharedPreferences(DashboardActivity.this.getApplicationContext(), Constants.SharedPrefConstants.KEY_TYPE);
            if (userType.equals(Constants.UserTypes.ADMIN)) {
                return Constants.NavigationView.ADMIN;
            }

            if (userType.equals(Constants.UserTypes.STUDENT)) {
                String userBranch = SharedPrefHelper.getEntryfromSharedPreferences(DashboardActivity.this.getApplicationContext(), Constants.SharedPrefConstants.KEY_BRANCH);
                if (userBranch != null)
                    allotStudentsToTopic(userBranch);
                else
                    Log.i("Info", "Null");
                return Constants.NavigationView.STUDENT;
            }
            return null;
        }
    }

    private void allotStudentsToTopic(String userBranch) {
        switch (userBranch) {
            case "Comp": {
                FirebaseMessaging.getInstance().subscribeToTopic("Comp");
                break;
            }

            case "Mech": {
                FirebaseMessaging.getInstance().subscribeToTopic("Mech");
                break;
            }

            case "Civil": {
                FirebaseMessaging.getInstance().subscribeToTopic("Civil");
                break;
            }

            case "MechSandwich": {
                FirebaseMessaging.getInstance().subscribeToTopic("MechSandwich");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            finish();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        Fragment fragment;
        switch (id) {
            case R.id.nav_placementdashboard: {
                toolbar.setTitle("Placement Dashboard");
                fragment = new PlacementDashboardFragment();
                startTransactionFragment(fragment);
                break;
            }

            case R.id.admin_navigation_drawer_send_notifications: {
                toolbar.setTitle("Send Notifications");
                fragment = new SendNotificationFragment();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.navigation_drawer_view_all_notifications: {
                toolbar.setTitle("View All Notifications");
                fragment = new ViewNotificationList();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.student_navigation_drawer_view_applications: {
                toolbar.setTitle("View Your Applications");
                fragment = new ViewYourApplicationsList();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.navigation_drawer_update_profile: {
                toolbar.setTitle("Update Profile");
                fragment = new UpdateProfile();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.admin_view_students_profile: {
                toolbar.setTitle("View Students Profile");
                fragment = new ViewStudentsProfile();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.navigation_drawer_prep_material: {
                toolbar.setTitle("View Students Profile");
                fragment = new PrepMaterialFragment();
                startTransactionFragment(fragment);
                break;
            }
            case R.id.log_out: {
                logOutFromApp();
            }

            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOutFromApp() {
        if (SharedPrefHelper.clearEntriesInSharedPrefs(this.getApplicationContext())) {
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    // 1 - Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_admin_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void startTransactionFragment(Fragment fragment) {
        if (fragment != null && !fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }
}
