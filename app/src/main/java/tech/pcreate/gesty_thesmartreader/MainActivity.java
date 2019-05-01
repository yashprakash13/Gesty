/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import tech.pcreate.gesty_thesmartreader.mainFragments.HomeFragment;
import tech.pcreate.gesty_thesmartreader.mainFragments.LibraryFragment;
import tech.pcreate.gesty_thesmartreader.settings.SettingsActivity;

import static com.github.florent37.runtimepermission.RuntimePermission.askPermission;
import static tech.pcreate.gesty_thesmartreader.utils.AppConstants.PATH;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /*Copyright (c) @ 2019 Yash Prakash
     */
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(this);
        navView.getMenu().getItem(0).setChecked(true);

        //Outer contexts
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data == null) loadFragment(new HomeFragment());
        else {
            Bundle bundle = new Bundle();
            bundle.putString("loc", data.getPath());
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);
            loadFragment(homeFragment);
        }

        checkPermissions();
    }

    private void makeSDCardFolderForLibrary() {
        File file = new File(PATH);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) Log.e("MainActivity", "Could not make directory");
        }
    }

    private void checkPermissions() {
        askPermission(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onAccepted((result) -> makeSDCardFolderForLibrary())
                .onDenied((result) -> {
                    Snackbar.make(navView, getString(R.string.permisision_needed_for_writing), Snackbar.LENGTH_SHORT).show();

                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.write_permission))
                            .setPositiveButton(getString(R.string.okay_string), (dialog, which) -> result.askAgain())
                            .setNegativeButton(getString(R.string.no_string), (dialog, which) -> dialog.dismiss())
                            .show();

                })
                .onForeverDenied((result) -> Snackbar.make(navView, getString(R.string.permisision_needed_for_writing), Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.go_to_settings), view -> result.goToSettings()).show())
                .ask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
            return true;
        }/*Copyright (c) @ 2019 Yash Prakash
         */
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.navigation_home) { loadFragment(new HomeFragment()); }
        else if (menuItem.getItemId() == R.id.navigation_my_library){ loadFragment(new LibraryFragment()); }
        return true;
    }
}
