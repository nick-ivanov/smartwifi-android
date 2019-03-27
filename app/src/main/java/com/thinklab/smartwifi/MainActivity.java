package com.thinklab.smartwifi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity implements HotspotFragment.OnHeadlineSelectedListener{
    final ConnectionFragment connectionFragment = new ConnectionFragment();
    final AboutFragment about = new AboutFragment();
    final SettingsFragment settings = new SettingsFragment();
    final HotspotFragment hotspot = new HotspotFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = connectionFragment;

    @Override
    public void onConnected(boolean connected) {
        connectionFragment.setConnectedStatus(connected);
    }
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


        fm.beginTransaction().hide(active).show(connectionFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, about).hide(about).commit();
        fm.beginTransaction().add(R.id.fragment_container, connectionFragment).hide(connectionFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, settings).hide(settings).commit();
        fm.beginTransaction().add(R.id.fragment_container, hotspot).hide(hotspot).commit();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }



}
