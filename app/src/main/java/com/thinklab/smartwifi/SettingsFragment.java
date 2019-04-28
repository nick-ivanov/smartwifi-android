package com.thinklab.smartwifi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.util.Collection;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.VISIBLE;

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
        walletAddress = (TextView) view.findViewById(R.id.walletAddress2);
        walletPrivate = (TextView) view.findViewById(R.id.walletPrivateKey2);

        File f = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath());
        Wallet wallet = new Wallet();
        Collection collection = getListOfAllConfigFiles(f);
        if(collection.isEmpty()){
            try {
                String fileName = wallet.createWallet();
                Log.d("Created wallet", " successfully");
                setWalletInfo(fileName);


            }catch (Exception e){
                Log.d("Failed", " creating wallet");
            }
        }
        else {
            String fileName2 = collection.iterator().next().toString();
            try {
                Credentials credentials = wallet.loadCredentials("password", fileName2);
                setWalletInfo(fileName2);

            } catch (Exception e) {
                Log.d("Wrong ", "filename");
            }
        }
        return view;
    }

    Collection<String> getListOfAllConfigFiles(File directory)
    {
        return FileUtils.listFiles(directory, new WildcardFileFilter("*.json"), null);
    }

    public void setWalletInfo(String fileName){
        HttpService infuraHttpService = new HttpService("https://ropsten.infura.io/v3/cdc0f13f944348f9a395ad4887d1e859");
        web3j = Web3j.build(infuraHttpService); //connect to infura with web3j
        try {
            Log.d("Shared fileName", fileName);
            credentials = wallet.loadCredentials("password", fileName);
            Log.d("Credentials, ", "Loaded");
            Log.d("Address: ", credentials.getAddress());
            walletAddress.setText(credentials.getAddress());
            walletPrivate.setText(credentials.getEcKeyPair().getPrivateKey().toString(16));
            Log.d("privatekey : ",credentials.getEcKeyPair().getPrivateKey().toString(16) );
        } catch (Exception e) {
            Log.d("hey, ", e.toString());
        }
    }
    public String getWalletAddress(){
        return this.walletAddress.getText().toString();
    }




}
