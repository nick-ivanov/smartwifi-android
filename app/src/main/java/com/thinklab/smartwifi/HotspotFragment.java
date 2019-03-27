package com.thinklab.smartwifi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.*;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import com.thanosfisherman.wifiutils.WifiUtils;

public class HotspotFragment extends Fragment {
    OnHeadlineSelectedListener callback;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private static WifiReceiver wifiReceiver;
    private ListView listView;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;


    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    public interface OnHeadlineSelectedListener {
        void onConnected(boolean connected);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();
        View view = inflater.inflate(R.layout.fragment_hotspots, container, false);
        listView = (ListView) view.findViewById(R.id.wifiList);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        setRetainInstance(true);
        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                final String ssid = listView.getItemAtPosition(position).toString();
                final EditText input = new EditText(getActivity());

                myAlertDialog.setTitle(listView.getItemAtPosition(position).toString());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                myAlertDialog.setView(input);
                myAlertDialog.setMessage("Please enter the password");

                myAlertDialog.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        WifiUtils.withContext(getContext()) //Attempt to connect
                                .connectWith(ssid, input.getText().toString())
                                .onConnectionResult(this::checkResult)
                                .start(); //Credit to https://github.com/ThanosFisherman/WifiUtils for this library
                    }
                    private void checkResult(boolean isSuccess) { //Credit to https://github.com/ThanosFisherman/WifiUtils for checkResult function, found in his example
                        Toast wifiSuccessToast = Toast.makeText(getActivity(),"Successfully connected", Toast.LENGTH_SHORT );
                        Toast wifiFailToast = Toast.makeText(getActivity(),"Failed to connect", Toast.LENGTH_LONG );
                        wifiFailToast.setGravity(Gravity.CENTER, 0 ,0);
                        wifiSuccessToast.setGravity(Gravity.CENTER, 0 ,0);
                        if (isSuccess) {
                            //callback.onConnected(true);
                            wifiSuccessToast.show();
                        }
                        else {
                            wifiFailToast.show();
                            //callback.onConnected(false);
                        }
                    }
                });
                myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                myAlertDialog.show();

            }
        });
        scanWifi(); //scan for AP's
        return view;
        }


        private void scanWifi () {
            arrayList.clear();
            getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
            Toast.makeText(getContext(), "Scanning", Toast.LENGTH_SHORT).show();
        }

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         * WifiReceiver Class gets all AP's and displays them in a     *
         * list. Does not display if there are no AP's or if an AP     *
         * has a blank SSID.                                           *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        class WifiReceiver extends BroadcastReceiver {

            public boolean isSmartConnection(String ssid){
                if (ssid.substring(0, 3).equals("swf")){
                    String newSSID = ssid.substring(3);
                    if (Patterns.IP_ADDRESS.matcher(newSSID).matches()) {
                        return true;
                    }
                }
                else {
                    return false; }
                    return false;
            }

            public void onReceive(Context c, Intent intent) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
                String action = intent.getAction();
                if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    results = wifiManager.getScanResults();
                    if (results.size() > 0) {
                        for (ScanResult scanResult : results) {
                            if (!scanResult.SSID.equals("")) {
                                //if (isSmartConnection(scanResult.SSID)) {
                                    arrayList.add(scanResult.SSID);
                                    adapter.notifyDataSetChanged();
                                //}
                            }
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "No Wifi found.", Toast.LENGTH_SHORT).show();

                }
            }

        }


        }

