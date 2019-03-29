package com.thinklab.smartwifi;

import android.content.SharedPreferences;
import android.os.Bundle;
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
    private Button button;
    private Button button2;
    private EditText textField;
    private TextView walletAddress;
    private TextView walletPrivate;
    private TextView walletSuccess;
    public Credentials credentials;
    Wallet wallet = new Wallet();
    Web3j web3j;
    static public String privateKey;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        button = (Button) view.findViewById(R.id.button2);
        textField = (EditText) view.findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWalletPrivate(getContext(), textField.getText().toString());
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        button2 = (Button) view.findViewById(R.id.createButton);
        walletAddress = (TextView) view.findViewById(R.id.walletAddress);
        walletPrivate = (TextView) view.findViewById(R.id.walletPrivateKey);
        walletSuccess = (TextView) view.findViewById(R.id.walletSuccess);
        walletSuccess.setVisibility(view.GONE);
        walletPrivate.setVisibility(view.GONE);
        walletAddress.setVisibility(view.GONE);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpService infuraHttpService = new HttpService("https://ropsten.infura.io/v3/cdc0f13f944348f9a395ad4887d1e859");
                web3j = Web3j.build(infuraHttpService); //connect to infura with web3j
                try {
                    String fileName = wallet.createWallet();
                    Log.d("Created, ", "Wallet @" + fileName);
                    credentials = wallet.loadCredentials("password", fileName);
                    Log.d("Credentials, ", "Loaded");
                    Log.d("Address: ", credentials.getAddress());
                    walletSuccess.setVisibility(view.VISIBLE);
                    walletAddress.setVisibility(view.VISIBLE);
                    walletPrivate.setVisibility(view.VISIBLE);
                    walletAddress.setText("Wallet Address : " + credentials.getAddress());
                    walletPrivate.setText("Wallet PrivateKey : " + credentials.getEcKeyPair().getPrivateKey().toString(16));
                    Log.d("privatekey : ",credentials.getEcKeyPair().getPrivateKey().toString(16) );
                } catch (Exception e) {
                    Log.d("hey, ", e.toString());
                }

            }
        });
        return view;
    }


    public static void setWalletPrivate(Context context, String username){
        SharedPreferences prefs = context.getSharedPreferences("myAppPackage", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("privateKey" , privateKey);
        editor.commit();

    }

    public static String getWalletPrivate(Context context){
        SharedPreferences prefs = context.getSharedPreferences("myAppPackage" , 0);
        return prefs.getString("privateKey", "");
    }

}
