package com.thinklab.smartwifi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


import java.util.ArrayList;
import java.util.List;

import com.thanosfisherman.wifiutils.WifiUtils;

public class HotspotFragment extends Fragment {
    private WifiManager wifiManager;
    private static WifiReceiver wifiReceiver;
    private ListView listView;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;


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
                                .start();
                    }
                    private void checkResult(boolean isSuccess) {
                        Toast wifiSuccessToast = Toast.makeText(getActivity(),"Successfully connected", Toast.LENGTH_SHORT );
                        Toast wifiFailToast = Toast.makeText(getActivity(),"Failed to connect", Toast.LENGTH_LONG );
                        wifiFailToast.setGravity(Gravity.CENTER, 0 ,0);
                        wifiSuccessToast.setGravity(Gravity.CENTER, 0 ,0);
                        if (isSuccess)
                            wifiSuccessToast.show();
                        else
                            wifiFailToast.show();
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
        scanWifi();
        return view;
        }


        private void scanWifi () {
            arrayList.clear();
            getActivity().registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifiManager.startScan();
            Toast.makeText(getContext(), "Scanning", Toast.LENGTH_SHORT).show();
        }


        class WifiReceiver extends BroadcastReceiver {
            public void onReceive(Context c, Intent intent) {
                String action = intent.getAction();
                if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    results = wifiManager.getScanResults();
                    if (results.size() > 0 && !results.equals("")) {
                        for (ScanResult scanResult : results) {
                            arrayList.add(scanResult.SSID);
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(getActivity(), "No Wifi found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


        }

}
