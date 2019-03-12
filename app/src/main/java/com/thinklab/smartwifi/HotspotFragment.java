package com.thinklab.smartwifi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HotspotFragment extends Fragment{
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
