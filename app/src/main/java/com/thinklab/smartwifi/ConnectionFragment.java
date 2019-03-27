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

public class ConnectionFragment extends Fragment {
    private TextView connectionBool;
    private TextView ipAddress;
    private TextView connectionTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        connectionBool = (TextView) view.findViewById(R.id.connectionBool2Text);

        setRetainInstance(true);


        return view;
    }
    public void setConnectedStatus(boolean connected) {
        if(connected){
            connectionBool.setText("Connected");
            connectionBool.setTextColor(Color.GREEN);
        }
        else{
            connectionBool.setText("Dissconnected");
        }
    }
}
