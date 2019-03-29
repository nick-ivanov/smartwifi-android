package com.thinklab.smartwifi;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.thinklab.smartwifi.HotspotFragment;
import com.thinklab.smartwifi.ConnectionFragment;
import android.view.SurfaceHolder.Callback;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    final ConnectionFragment connectionFragment = new ConnectionFragment();
    final AboutFragment about = new AboutFragment();
    final SettingsFragment settings = new SettingsFragment();
    final HotspotFragment hotspot = new HotspotFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = connectionFragment;


    private TextView mTextMessage;




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_connection:
                    fm.beginTransaction().hide(active).show(connectionFragment).commit();
                    active = connectionFragment;
                    return true;
                case R.id.navigation_hotspots:
                    fm.beginTransaction().hide(active).show(hotspot).commit();
                    active = hotspot;
                    return true;
                case R.id.navigation_settings:
                    fm.beginTransaction().hide(active).show(settings).commit();
                    active = settings;
                    return true;
                case R.id.navigation_about:
                    fm.beginTransaction().hide(active).show(about).commit();
                    active = about;
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //fm.beginTransaction().replace(R.id.fragment_container, connectionFragment).commit();

        fm.beginTransaction().add(R.id.fragment_container, about, "about").hide(about).commit();
        fm.beginTransaction().add(R.id.fragment_container, connectionFragment, "connection").commit();
        fm.beginTransaction().add(R.id.fragment_container, settings, "settings").hide(settings).commit();
        fm.beginTransaction().add(R.id.fragment_container, hotspot, "hotspot").hide(hotspot).commit();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }

    private void checkPermissions() {
        String TAG = "Permisiion";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted1");
            } else {
                Log.v(TAG, "Permission is revoked1");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted1");
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
            } else {
                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
        }
    }



}
