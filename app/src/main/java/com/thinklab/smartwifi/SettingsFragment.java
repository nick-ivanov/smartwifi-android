package com.thinklab.smartwifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class SettingsFragment extends Fragment {
    private TextView walletAddress;
    private TextView walletPrivate;
    public Credentials credentials;
    Wallet wallet = new Wallet();
    Web3j web3j;
    static public String privateKey;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
                HttpService infuraHttpService = new HttpService("https://ropsten.infura.io/v3/cdc0f13f944348f9a395ad4887d1e859");
                web3j = Web3j.build(infuraHttpService); //connect to infura with web3j
                walletAddress = (TextView) view.findViewById(R.id.walletAddress2);
                walletPrivate = (TextView) view.findViewById(R.id.walletPrivateKey2);
                try {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                    String fileName = preferences.getString("fileName", null);
                    Log.d("Shared fileName", fileName);
                    credentials = wallet.loadCredentials("password", fileName);
                    Log.d("Credentials, ", "Loaded");
                    Log.d("Address: ", credentials.getAddress());
                    walletAddress.setVisibility(view.VISIBLE);
                    walletPrivate.setVisibility(view.VISIBLE);
                    walletAddress.setText(credentials.getAddress());
                    walletPrivate.setText(credentials.getEcKeyPair().getPrivateKey().toString(16));
                    Log.d("privatekey : ",credentials.getEcKeyPair().getPrivateKey().toString(16) );
                } catch (Exception e) {
                    Log.d("hey, ", e.toString());
                }

        return view;
    }


}
