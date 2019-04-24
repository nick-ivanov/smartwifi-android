package com.thinklab.smartwifi;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Button;
import android.widget.Button;
import android.os.SystemClock;

public class ConnectionFragment extends Fragment {
    private TextView connectionBool;
    private TextView ipAddress;
    private TextView connectionTime;
    private Button button1;

    private Chronometer timetotalChrono;
    private Chronometer simpleChronometer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_connection, container, false);



        timetotalChrono = (Chronometer) view.findViewById(R.id.simpleChronometer1);
        timetotalChrono.setBase(SystemClock.elapsedRealtime() - 1074000);

        simpleChronometer = (Chronometer) view.findViewById(R.id.simpleChronometer);
        simpleChronometer.setBase(SystemClock.elapsedRealtime() - 788000);

        connectionBool = (TextView) view.findViewById(R.id.message12);
        button1 = (Button) view.findViewById(R.id.button1);
        ipAddress = (TextView) view.findViewById(R.id.message22);
        setRetainInstance(true);

        //button1.setVisibility();



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConnectedStatus(false);
                button1.setVisibility(View.GONE);
                setConnectionClock(false);
                setIpAddress(false, "");

            }
        });


        return view;
    }
    public void setConnectedStatus(boolean connected) {
        if(connected){
            connectionBool.setText("Connected");
            connectionBool.setTextColor(Color.GREEN);
        }
        else{
            connectionBool.setText("Dissconnected");
            connectionBool.setTextColor(Color.BLACK);
        }
    }

    public void showDisconnect(boolean show){
        button1.setVisibility(View.VISIBLE);
    }

    public void setIpAddress(boolean connected, String ip){
        if(connected){
            ipAddress.setText("IP: " + ip);
            ipAddress.setTextColor(Color.GREEN);
        }
        else{
            ipAddress.setText("IP: NONE");
            ipAddress.setTextColor(0xFFBCBCBC);
        }
    }
    public void setConnectionClock(boolean connected) {
        if(connected){
            //simpleChronometer.setBase(SystemClock.elapsedRealtime() - 788000);
            simpleChronometer.start();
        }
        else {
            //simpleChronometer.setBase(SystemClock.elapsedRealtime() - 788000);
            simpleChronometer.stop();
        }

    }

}
