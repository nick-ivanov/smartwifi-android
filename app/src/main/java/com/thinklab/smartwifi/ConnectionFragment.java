package com.thinklab.smartwifi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Chronometer;
import android.widget.Button;
import android.widget.Button;
import android.os.SystemClock;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.jsoup.Jsoup;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;

import static java.lang.String.valueOf;

public class ConnectionFragment extends Fragment {
    final Handler timerHandler = new Handler();
    public static ConnectionFragment C1;
    private TextView connectionBool;
    private TextView ipAddress;
    private TextView connectionTime;
    private Button button1;
    private static TextView smartContractAddress;
    private static TextView connectionSpeed;
    private TextView etherWallet;
    SWFHelper swfHelper = new SWFHelper();
    private static Chronometer timetotalChrono;
    private static Chronometer simpleChronometer;
    private TextView wifissid;
    private static TextView totalCharge;
    private static int flag = 0;
    private static FragmentActivity mInstance;
    private static long timeWhenStopped = 0;
    private static TextView smartStatus;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        mInstance = getActivity();
        etherWallet = (TextView) view.findViewById(R.id.message92);
        timetotalChrono = (Chronometer) view.findViewById(R.id.simpleChronometer1);
        timetotalChrono.setBase(SystemClock.elapsedRealtime());
        totalCharge = (TextView) view.findViewById(R.id.message82);
        simpleChronometer = (Chronometer) view.findViewById(R.id.simpleChronometer);
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        smartContractAddress = (TextView) view.findViewById(R.id.message112);
        smartStatus = (TextView) view.findViewById(R.id.message122);
        wifissid = (TextView) view.findViewById(R.id.message02);
        wifissid.setText("");

        connectionBool = (TextView) view.findViewById(R.id.messageActive);
        button1 = (Button) view.findViewById(R.id.button1);
        ipAddress = (TextView) view.findViewById(R.id.message22);
        connectionSpeed = (TextView) view.findViewById(R.id.message32);
        setRetainInstance(true);
        connectionBool.setText("Dissconnected");
        button1.setVisibility(View.GONE);
        connectionSpeed.setText("Calculating");
        connectionSpeed.setTextColor(Color.GREEN);
        totalCharge.setText("0.000 ETH");
        smartContractAddress.setText("");
        connectionSpeed.setText("N/A");
        ipAddress.setText("");
        smartStatus.setText("");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setConnectedStatus(false);
                button1.setVisibility(View.GONE);
                setConnectionClock(false);
                setIpAddress(false, "");
                smartContractAddress.setText("");
                totalCharge.setText("0.000 ETH");
                connectionSpeed.setText("N/A");
                ipAddress.setText("N/A");
                //WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                //wifiManager.setWifiEnabled(false);


            }
        });


        return view;
    }
    public void setConnectedStatus(boolean connected) {
        if(connected) {
            connectionBool.setText("Active");
            connectionBool.setTextColor(Color.GREEN);
        }
        else{
            connectionBool.setText("Dissconnected");
            connectionBool.setTextColor(Color.BLACK);
        }
    }


    public static void setSpeed(Double speedTmp){
        speed = speedTmp;
        Log.d("speed is ", valueOf(speed));

    }

    public void showDisconnect(boolean show){
        button1.setVisibility(View.VISIBLE);
    }

    public void setSSID(String ssid){
        wifissid.setText(ssid);
    }

    public void setWallet(String addr){
        this.etherWallet.setText(addr);
    }

    public void setIpAddress(boolean connected, String ip){
        if(connected){
            ipAddress.setText(ip);
            ipAddress.setTextColor(Color.GREEN);
        }
        else{
            ipAddress.setText("IP: NONE");
            ipAddress.setTextColor(0xFFBCBCBC);
        }
    }

//    Runnable updater;
//
//    void updateTime(final String timeString) {
//        final Handler handler=new Handler();
//        updater = new Runnable() {
//            @Override
//            public void run() {
//                long elapsedMillis = SystemClock.elapsedRealtime() - simpleChronometer.getBase();dfops;jn
//                //Log.d("elappsed time", valueOf(elapsedMillis));
//                String charge = timeString + valueOf(0.0012);
//                totalCharge.setText(charge + "ETH");
//                timerHandler.postDelayed(updater,600);
//            }
//        };
//        timerHandler.post(updater);
//    }
    public void setConnectionClock(boolean connected) {

        if(connected){
            simpleChronometer.setBase(SystemClock.elapsedRealtime());
            timetotalChrono.setBase(SystemClock.elapsedRealtime());
            simpleChronometer.start();
            timetotalChrono.start();
            //updateTime("0.0000 ETH");
        }
        else {
            simpleChronometer.setBase(SystemClock.elapsedRealtime());
            timetotalChrono.setBase(SystemClock.elapsedRealtime());
            simpleChronometer.stop();
            timetotalChrono.stop();
            //timerHandler.removeCallbacks(updater);

        }

    }



        static volatile double speed;

        static String getHashChain(String seed, int n) {
            Keccak.DigestKeccak kecc = new Keccak.Digest256();
            byte[] input = seed.getBytes();
            kecc.update(input, 0, input.length);

            byte[] current = kecc.digest();

            for(int i = 0; i < n; i++) {
                byte[] prev = current;

                kecc.update(prev, 0, prev.length);
                current = kecc.digest();
            }

            System.out.println("CURRENT:: " + Arrays.toString(current));

            return Numeric.toHexString(current);
        }

        static String getHashChainFromBytes(byte[] input, int n) {
            Keccak.DigestKeccak kecc = new Keccak.Digest256();
            kecc.update(input, 0, input.length);

            byte[] current = kecc.digest();

            for(int i = 0; i < n; i++) {
                byte[] prev = current;

                kecc.update(prev, 0, prev.length);
                current = kecc.digest();
            }

            return Numeric.toHexString(current);
        }

        static boolean verifyHash(String top, String hash, int n) {
            if(n == -1) {
                return true;
            }

            byte[] byte_hash = Numeric.hexStringToByteArray(hash.replaceAll("0x", ""));
            String res = com.thinklab.smartwifi.SWFHelper.getHashChainFromBytes(byte_hash, n);

//        System.out.println("n: " + n);
//        System.out.println("verifyHash: top: " + top);
//        System.out.println("verifyHash: res: " + res);
//        System.out.println("verifyHash: hash: " + hash);
//        System.out.println("checking whether " + res + " == " + top);


            if(res.equals(top)) {
                return true;
            }

            return false;
        }

        static ClientSecret generateClientSecret() {
            ClientSecret secret = new ClientSecret();

            UUID uuid = UUID.randomUUID();
            String seed = uuid.toString();
            secret.setSeed(seed);
            int chainsize = 60;
            secret.setSize(chainsize);
            ArrayList<String> hchain = getFullHashChain(seed, chainsize);
            secret.setHashchain(hchain);
            secret.setTop(hchain.get(59));

            return secret;
        }

        static ArrayList<String> getFullHashChain(String seed, int n) {
            ArrayList<String> hashes = new ArrayList<>();
            Keccak.DigestKeccak kecc = new Keccak.Digest256();
            byte[] input = seed.getBytes();
            kecc.update(input, 0, input.length);
            byte[] current = kecc.digest();

            for(int i = 0; i < n; i++) {
                byte[] prev = current;

                kecc.update(prev, 0, prev.length);
                current = kecc.digest();
                hashes.add(Numeric.toHexString(current));
            }

            return hashes;
        }

        static void nbSleep(long subperiod_msec) {
            try {
                Thread.sleep(subperiod_msec);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }

        static void activateInternet(String ip) {
            System.out.println("Activating Internet for IP: " + ip);
        }

        static void deactivateInternet(String ip) {
            System.out.println("Deactivating Internet for IP: " + ip);
        }

        // Couresy: https://stackoverflow.com/a/8263301/8041645
        static long getTimestamp() {
            Log.d("Calling ", "Get time stamp");
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.clear();
            calendar.setTime(new Date());
            return calendar.getTimeInMillis() / 1000L;
        }


        static long getTimestampMillis() {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.clear();
            calendar.setTime(new Date());
            return calendar.getTimeInMillis();
        }


        static String contractGetHash(Web3j web3, String from, String contract) {
            List<Type> inputParameters1 = new ArrayList<>();
            List<TypeReference<?>> outputParameters1 = new ArrayList<>();


            outputParameters1.add(new TypeReference<Bytes32>() {});

            Function function1 = new Function("getHash",
                    inputParameters1,
                    outputParameters1);
            String functionEncoder1 = FunctionEncoder.encode(function1);


            EthCall response = null;

            try {
                response = web3.ethCall(Transaction.createEthCallTransaction(from, contract, functionEncoder1), DefaultBlockParameterName.LATEST).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            List<Type> someType = FunctionReturnDecoder.decode(response.getValue(),function1.getOutputParameters());
            Iterator<Type> it = someType.iterator();
            Type resault = someType.get(0);
            Bytes32 a = (Bytes32) resault;

            return response.getValue();
        }

        static String contractSetHash(Web3j web3, Credentials credentials, String from, String contract, String hash) {
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(new Bytes32(Numeric.hexStringToByteArray(hash.replaceAll("0x", ""))));

            List<TypeReference<?>> outputParameters = new ArrayList<>();

            Function function = new Function("setHash",
                    inputParameters,
                    outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount;
            BigInteger nonce;

            try {
                ethGetTransactionCount = web3.ethGetTransactionCount(
                        from, DefaultBlockParameterName.LATEST).send();
                nonce = ethGetTransactionCount.getTransactionCount();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            RawTransaction rawTransaction  = RawTransaction.createTransaction(
                    nonce, Wallet.GAS_PRICE_DEFAULT, Wallet.GAS_LIMIT_DEFAULT, contract, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction rawTrasactionResponse;


            try {
                rawTrasactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            return rawTrasactionResponse.getTransactionHash();
        }

        static String contractFund(Web3j web3, String wallet_public, String contract, Credentials credentials, String price) {
            Wallet wallet = new Wallet();
            try {
                return wallet.sendTransaction(web3, wallet_public, contract, credentials, price);
            } catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        static String contractPay(Web3j web3, Credentials credentials, String from, String contract, String hash, int n) {
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(new Bytes32(Numeric.hexStringToByteArray(hash.replaceAll("0x", ""))));

            BigInteger k = new BigInteger(String.valueOf(n));
            inputParameters.add(new Uint(k));

            List<TypeReference<?>> outputParameters = new ArrayList<>();


            //outputParameters.add(new TypeReference<Utf8String>() {});

            Function function = new Function("pay",
                    inputParameters,
                    outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount;
            BigInteger nonce;

            try {
                ethGetTransactionCount = web3.ethGetTransactionCount(
                        from, DefaultBlockParameterName.LATEST).send();
                nonce = ethGetTransactionCount.getTransactionCount();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            RawTransaction rawTransaction  = RawTransaction.createTransaction(
                    nonce, Wallet.GAS_PRICE_DEFAULT, Wallet.GAS_LIMIT_DEFAULT, contract, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction rawTrasactionResponse;

            try {
                rawTrasactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            String transactionHash = null;
            int timeout = 5;
            while (null == transactionHash && timeout != 0) {
                transactionHash = rawTrasactionResponse.getTransactionHash();
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                timeout--;
            }

//        System.out.println("Transaction hash: " + rawTrasactionResponse.getTransactionHash());
//        System.out.println("tr.toString(): " + rawTrasactionResponse.toString());
//        System.out.println("tr.getJsonrpc(): " + rawTrasactionResponse.getJsonrpc());
//        System.out.println("tr.getRawResponse(): " + rawTrasactionResponse.getRawResponse());
//        System.out.println("tr.getResult(): " + rawTrasactionResponse.getResult());
//        System.out.println("tr.hasError(): " + rawTrasactionResponse.hasError());
//
//        if(rawTrasactionResponse.hasError()) {
//            System.out.println("tr.error: " + rawTrasactionResponse.getError());
//            System.out.println("tr.error: " + rawTrasactionResponse.getError().getMessage());
//        }

            return rawTrasactionResponse.getTransactionHash();
        }


        static String contractRefund(Web3j web3, Credentials credentials, String from, String contract, String hash) {
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(new Bytes32(Numeric.hexStringToByteArray(hash.replaceAll("0x", ""))));

            List<TypeReference<?>> outputParameters = new ArrayList<>();


            //outputParameters.add(new TypeReference<Utf8String>() {});

            Function function = new Function("refund",
                    inputParameters,
                    outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount;
            BigInteger nonce;

            try {
                ethGetTransactionCount = web3.ethGetTransactionCount(
                        from, DefaultBlockParameterName.LATEST).send();
                nonce = ethGetTransactionCount.getTransactionCount();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            RawTransaction rawTransaction  = RawTransaction.createTransaction(
                    nonce, Wallet.GAS_PRICE_DEFAULT, Wallet.GAS_LIMIT_DEFAULT, contract, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction rawTrasactionResponse;

            try {
                rawTrasactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            String transactionHash = null;
            int timeout = 5;
            while (null == transactionHash && timeout != 0) {
                transactionHash = rawTrasactionResponse.getTransactionHash();
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                timeout--;
            }

//        System.out.println("Transaction hash: " + rawTrasactionResponse.getTransactionHash());
//        System.out.println("tr.toString(): " + rawTrasactionResponse.toString());
//        System.out.println("tr.getJsonrpc(): " + rawTrasactionResponse.getJsonrpc());
//        System.out.println("tr.getRawResponse(): " + rawTrasactionResponse.getRawResponse());
//        System.out.println("tr.getResult(): " + rawTrasactionResponse.getResult());
//        System.out.println("tr.hasError(): " + rawTrasactionResponse.hasError());
//
//        if(rawTrasactionResponse.hasError()) {
//            System.out.println("tr.error: " + rawTrasactionResponse.getError());
//            System.out.println("tr.error: " + rawTrasactionResponse.getError().getMessage());
//        }

            return rawTrasactionResponse.getTransactionHash();
        }


        static String contractRefund(Web3j web3, Credentials credentials, String from, String contract, String hash, int n) {

            return null;
        }

        static String contractSetAddress(Web3j web3, Credentials credentials, String from, String contract, String address) {
            List<Type> inputParameters = new ArrayList<>();

            inputParameters.add(new Address(address));

            List<TypeReference<?>> outputParameters = new ArrayList<>();

            Function function = new Function("setAddress",
                    inputParameters,
                    outputParameters);

            String encodedFunction = FunctionEncoder.encode(function);

            EthGetTransactionCount ethGetTransactionCount;
            BigInteger nonce;

            try {
                ethGetTransactionCount = web3.ethGetTransactionCount(
                        from, DefaultBlockParameterName.LATEST).send();
                nonce = ethGetTransactionCount.getTransactionCount();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            RawTransaction rawTransaction  = RawTransaction.createTransaction(
                    nonce, Wallet.GAS_PRICE_DEFAULT, Wallet.GAS_LIMIT_DEFAULT, contract, encodedFunction);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction rawTrasactionResponse;

            try {
                rawTrasactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            return rawTrasactionResponse.getTransactionHash();
        }




        static String contractGetAddress(Web3j web3, String from, String contract) {
            List<Type> inputParameters1 = new ArrayList<>();
            List<TypeReference<?>> outputParameters1 = new ArrayList<>();

            outputParameters1.add(new TypeReference<Address>() {});

            Function function1 = new Function("getAddress",
                    inputParameters1,
                    outputParameters1);
            String functionEncoder1 = FunctionEncoder.encode(function1);


            EthCall response = null;

            try {
                response = web3.ethCall(Transaction.createEthCallTransaction(from, contract, functionEncoder1), DefaultBlockParameterName.LATEST).sendAsync().get();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

            List<Type> someType = FunctionReturnDecoder.decode(response.getValue(),function1.getOutputParameters());
            Iterator<Type> it = someType.iterator();
            Type resault = someType.get(0);
            Address a = (Address) resault;

            System.out.println("Address: " + a.toString());
            System.out.println("Another: " + a.toUint160().toString());

            return response.getValue();
        }


        static String getPage(String address) {

            try {
                return Jsoup.connect("http://" + address + ":85/payload10k.txt").get().html();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }


        }

        static String getSmallPage(String address) {
            try {
                return Jsoup.connect("http://" + address + ":85/oldpayload.txt").get().html();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }
        static void dupsetProbe1(){

        }

        public static void dupsetProbe() {

            final int buf_len = 1024 * 1024;
            final int small_buf_len = 1024;
            final int num_sites = 10;


            ArrayList<String> sites = new ArrayList<>();

            Log.d("Adding sites", "");

            sites.add("157.230.151.37");
            sites.add("178.62.109.110");
            sites.add("149.28.242.163");
            sites.add("207.148.87.126");
            sites.add("202.182.105.19");
            sites.add("68.183.89.82");
            sites.add("165.227.40.213");
            sites.add("188.166.87.44");
            sites.add("45.63.21.94");
            sites.add("68.183.236.197");

            String buf;

//        char * buf = (char *) malloc (BUF_LEN);
//        if (buf == NULL) {
//            fprintf (stderr, "malloc() error\n");
//            return 1;
//        }


            String buf_small;

//        char * buf_small = (char *) malloc (SMALL_BUF_LEN);
//        if (buf_small == NULL) {
//            fprintf (stderr, "malloc() error\n");
//            return 1;
//        }

            double res_maxspeed;
            long res_delay;
            long res_payload;
            double res_avg_latency;
            double sma2, sma3, sma4, sma5, sma6;
            double lsma2, lsma3, lsma4, lsma5, lsma6;
            double tcma;

            //double speeds[60];
            //double lspeeds[60];

            double[] speeds = new double[60];
            double[] lspeeds = new double[60];



            for(int k = 0; k < 120; k++) {

                sma2 = 0.0;
                sma3 = 0.0;
                sma4 = 0.0;
                sma5 = 0.0;
                sma6 = 0.0;

                lsma2 = 0.0;
                lsma3 = 0.0;
                lsma4 = 0.0;
                lsma5 = 0.0;
                lsma6 = 0.0;

                tcma = 0.0;



                double speedsum = 0.0;

                //printf("Number of sites: %d\n", NUM_SITES);

                //char* data;

                String data;

                long time0 = getTimestampMillis();
                long pload_sum = 0;
                double speedmax = 0.0;
                double sum_latency = 0.0;
                double delta;

                long time1, time2, time3, time4, time5;

                int ret;

                for(int i = 0; i < num_sites; i++) {
                    time1 = getTimestampMillis();
                    buf = getPage(sites.get(i));


                    time2 = getTimestampMillis();

                    delta = 0;

                    delta = 0;
                    if(buf != null) {
                        delta = buf.length();
                    }

                    //printf("ORIG. DELTA: %d\n", delta);

                    //sleep(2);

                    time3 = getTimestampMillis();

                    buf_small = getSmallPage(sites.get(i));
                    time4 = getTimestampMillis();

                    if(buf_small == null) delta = 0.0;
                    else delta -= buf_small.length();

                    //printf("ADJ. DELTA: %d\n", delta);

                    pload_sum += delta;


                    long tdelta = (time2 - time1) - (time4 - time3);
                    Log.d("Line", "648");
                    double speed;
                    if(tdelta <= 0) {
                        speed = 0.0;
                    } else {
                        speed = (double) ( (delta * 8.0) / (tdelta / 1000.0));
                    }
                    Log.d("Line", "655");

                    //printf("Site #%d (%s) payload delta (bytes): %d, delay delta (ms): %ld, speed (bps): %f, latency component (ms): %ld\n", (i+1), sites[i], delta, tdelta, speed, time4-time3);

                    sum_latency += (time4 - time3);

                    speedsum += speed;

                    if(speed > speedmax) {
                        speedmax = speed;
                    }
                }

                time5 = getTimestampMillis();

                //double speedavg = (double) (speedsum / (double) nsites);
                //printf("AVERAGE SPEED (bps): %f\n", speedavg);

                //printf("MAXIMUM SPEED (mbps): %f\n", speedmax/1000000.0);

                //printf("SPEED MEASUREMENT DELAY (msec): %ld\n", time5 - time0);
                //printf("TOTAL PAYLOAD SIZE (bytes): %ld\n", pload_sum);

                res_maxspeed = speedmax/1000000.0;
                speeds[k] = res_maxspeed;
                Log.d("Line", "680");
                res_delay = time5 - time0;
                res_payload = pload_sum;
                res_avg_latency = sum_latency / num_sites;
                Log.d("Line", "684");

                lspeeds[k] = res_avg_latency;

                double sma_sum = 0.0;
                double lsma_sum = 0.0;
                double y = 0.0;
                for(int z = k; z >= 0 && y < 2; z--, y++) {
                    sma_sum += speeds[z];
                    lsma_sum += lspeeds[z];
                    //printf("adding %f to SMA2\n", speeds[z]);
                }
                Log.d("Line", "696");
                sma2 = sma_sum/y;
                lsma2 = lsma_sum/y;
                Log.d("Line", "699");
                sma_sum = 0.0;
                lsma_sum = 0.0;
                y = 0.0;
                for(int z = k; z >= 0 && y < 3; z--, y++) {
                    sma_sum += speeds[z];
                    lsma_sum += lspeeds[z];
                    //printf("adding %f to SMA3\n", speeds[z]);
                }
                Log.d("Line", "708");
                sma3 = sma_sum/y;
                lsma3 = lsma_sum/y;
                Log.d("Line", "711");

                sma_sum = 0.0;
                lsma_sum = 0.0;
                y = 0.0;
                for(int z = k; z >= 0 && y < 4; z--, y++) {
                    sma_sum += speeds[z];
                    lsma_sum += lspeeds[z];
                    //printf("adding %f to SMA4\n", speeds[z]);
                }
                Log.d("Line", "721");
                sma4 = sma_sum/y;
                lsma4 = lsma_sum/y;
                Log.d("Line", "724");
                sma_sum = 0.0;
                lsma_sum = 0.0;
                y = 0.0;
                for(int z = k; z >= 0 && y < 5; z--, y++) {
                    sma_sum += speeds[z];
                    lsma_sum += lspeeds[z];
                    //printf("adding %f to SMA5\n", speeds[z]);
                }
                Log.d("Line", "733");
                sma5 = sma_sum/y;
                lsma5 = lsma_sum/y;
                Log.d("Line", "736");

                sma_sum = 0.0;
                lsma_sum = 0.0;
                y = 0.0;
                for(int z = k; z >= 0 && y < 6; z--, y++) {
                    sma_sum += speeds[z];
                    lsma_sum += lspeeds[z];
                    //printf("adding %f to SMA6\n", speeds[z]);
                }
                Log.d("Line", "746");
                sma6 = sma_sum/y;
                lsma6 = lsma_sum/y;
                Log.d("Line", "749");
                sma_sum = 0.0;

                //printf("k = %d\n", k);
                for(int z = k; z >= 0; z--) {
                    sma_sum += speeds[z];
                    //printf(".");
                    //printf("adding %f to SMA6\n", speeds[z]);
                }
                //printf("\n");
                Log.d("Line", "759");
                tcma = sma_sum/(k+1);
                Log.d("Line", "761");

                speed = tcma;

                try {
                    mInstance.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String speedtemp = String.format("%.2f", speed);
                            connectionSpeed.setText(speedtemp + " Mbps");
                            setSpeed(speed);
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("SPEED_REPORT: " + tcma);
                //SWFHelper.nbSleep(1000);
            }
        }

        public static void startDupset() {
            Runnable task = () -> {
                dupsetProbe();
            };

            Thread worker = new Thread(task);
            worker.start();
        }


        public static double getSpeed() {
            return speed;
        }

//
//        return 1;
//
//
//    }


    public static void run2() throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("started main", "Started main");
                    //SWFLogger.separator("hashchain");
                    //Log.d(SWFHelper.getFullHashChain("hello", 10).toString(), "");
                    //System.out.println(SWFHelper.getFullHashChain("hello", 10));

                    ClientSecret secret = SWFHelper.generateClientSecret();

                    //SWFLogger.log("hashchain", "secret class object", secret.toString());

                    //Log.d(secret.toString(), "");
                    //String walletfile = "/storage/emulated/0/Download/UTC--2017-10-20T19-02-52.7Z--c89fd8f8550efeef20a9fd53338a1f0f3a57c0a1.json";       //NEED TO REPLACE
                    //String walletpassword = "password";
                    //Credentials credentials = WalletUtils.loadCredentials(walletpassword, walletfile);




                    SettingsFragment settingsFragment = new SettingsFragment();
                    String privateKey = settingsFragment.getWalletPrivate();
                    String wallet_public = settingsFragment.getWalletAddress();
                    Log.d(privateKey, "Private key success");
                    Credentials credentials = settingsFragment.getCredentials();
                    //Log.d("Credentials", credentials.toString());
                    //System.out.println("CREDENTIALS: " + credentials.toString());
                    Log.d("Got Credentials", "Got Credentials");
                    String port = "5566";
                    String server_host = "10.42.0.1";
                    Log.d("Attempting Socket", "now");
                    // [[ SEND: TRANSMISSION #0 ]]
                    try (Socket socket = new Socket(server_host, Integer.parseInt(port))) {
                        Scanner in = new Scanner(socket.getInputStream());
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                        //NEED TO REPLACE
                        Log.d("public", wallet_public);
                        // [[ RECEIVE: TRANSMISSION #1 ]]
                        System.out.println(in.nextLine());
                        //Log.d(in.nextLine(), "Transmission 1");
                        // [[ SEND: TRANSMISSION #2 ]]
                        out.println("hello:" + wallet_public + ":" + secret.getTop());
                        //Log.d("hello", wallet_public + ":" + secret.getTop());

                        // [[ RECEIVE: TRANSMISSION #3 ]]
                        String reply = in.nextLine();
                        reply = reply.replaceAll("\n", "");
                        String[] aaa = reply.split(":");
                        mInstance.runOnUiThread (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String temp1 = aaa[1].substring(0,8);
                                String temp2 = aaa[1].substring(aaa[1].length() - 6);
                                String addr = temp1 + "..." + temp2;
                                smartContractAddress.setText(addr);
                            }
                        }));

                        System.out.println("REPLY: " + Arrays.asList(aaa));
                        //Log.d("REPLY: " + Arrays.asList(aaa), "Recieved");
                        String infura_api_key = "ef1ce1202d8248a69d1eacd3a6237f28"; //Might need to replace!

                        Web3j web3 = Web3j.build(new HttpService("https://ropsten.infura.io/v3/" + infura_api_key));  // defaults to http://localhost:8545/


                        //Credentials credentials = WalletUtils.loadCredentials("password", "/path/to/walletfile");

            /*TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3, credentials, aaa[1],
                    BigDecimal.valueOf(0.01), Convert.Unit.ETHER).send(); */


                        //Wallet wallet = new Wallet();

                        String contract = aaa[1];
                        String price = aaa[2];

                        String max_price = "0.002";

                        if (Double.parseDouble(price) > Double.parseDouble(max_price)) {
                            out.println("RIPOFF");
                            out.println("bye");
                            socket.close();
                            return;
                        }

                        SWFHelper.contractFund(web3, wallet_public, contract, credentials, price);

                        //String result = wallet.sendTransaction(web3, wallet_public, contract, credentials, price);

                        // [[ SEND: TRANSMISSION #4 ]]
                        out.println("OK");
                        //Log.d("OK", "");


                        // MESSAROUND


//            List<Type> inputParameters = new ArrayList<>();
//            List<TypeReference<?>> outputParameters = new ArrayList<>();
//
//
//            outputParameters.add(new TypeReference<Utf8String>() {});
//
//            Function function = new Function("renderHelloWorld",
//                    inputParameters,
//                    outputParameters);
//            String functionEncoder = FunctionEncoder.encode(function);
//
//            EthCall response = web3.ethCall(Transaction.createEthCallTransaction("0xe57EB9F6f71B4f9F37e6aB730942851d067d402E","0x03f081e292a6645d5bbd2017ca035175cd6a0b05", functionEncoder), DefaultBlockParameterName.LATEST).sendAsync().get();
//
////            Type resault = someType.get(0);
////
////            String a = resault.toString();
//
//
//
//            List<Type> someType = FunctionReturnDecoder.decode(response.getValue(),function.getOutputParameters());
//            Iterator<Type> it = someType.iterator();
//            Type resault = someType.get(0);
//            String a = resault.toString();
//
//            System.out.println("Hello0: " + a);
//            System.out.println("Hello0.1: " + response.toString());
//            System.out.println("Hello1: " + response.getValue());
//            System.out.println("Hello2: " + response.getResult());
//            System.out.println("Hello3: " + response.getRawResponse());


                        // END-OF-MESSAROUND


                        // MESSAROUND 1

//            System.out.println("blah: " + secret.getTop().replaceAll("0x", ""));
//
//
//
//
//            List<Type> inputParameters = new ArrayList<>();
//            inputParameters.add(new Bytes32(Numeric.hexStringToByteArray(secret.getTop().replaceAll("0x", ""))));
//
//
//            List<TypeReference<?>> outputParameters = new ArrayList<>();
//
//
//            //outputParameters.add(new TypeReference<Utf8String>() {});
//
//            Function function = new Function("setHash",
//                    inputParameters,
//                    outputParameters);
//            String functionEncoder = FunctionEncoder.encode(function);
//
//            EthCall response = web3.ethCall(Transaction.createEthCallTransaction("0xe57EB9F6f71B4f9F37e6aB730942851d067d402E","0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718", functionEncoder), DefaultBlockParameterName.LATEST).sendAsync().get();
//
////            Type resault = someType.get(0);
////
////            String a = resault.toString();
//
//
//
//            //List<Type> someType = FunctionReturnDecoder.decode(response.getValue(),function.getOutputParameters());
//            //Iterator<Type> it = someType.iterator();
//            //Type resault = someType.get(0);
//            //String a = resault.toString();
//
//
//            //This guy: 7e5fd2767e1a8499bfc8672ef9c9d7d0265c5a965978ee7bed78ea705f44b11b
//
//            //System.out.println("Hello0: " + a);
//            System.out.println("Hello0.1: " + response.toString());
//            System.out.println("Hello1: " + response.getValue());
//            System.out.println("Hello2: " + response.getResult());
//            System.out.println("Hello3: " + response.getRawResponse());

                        // END OF MESSAROUND 1


                        // MESSAROUND 1.5

//            System.out.println("top: " + secret.getTop());
//
//            List<Type> inputParameters = new ArrayList<>();
//            inputParameters.add(new Bytes32(Numeric.hexStringToByteArray(secret.getTop().replaceAll("0x", ""))));
//
//
//            List<TypeReference<?>> outputParameters = new ArrayList<>();
//
//
//            //outputParameters.add(new TypeReference<Utf8String>() {});
//
//            Function function = new Function("setHash",
//                    inputParameters,
//                    outputParameters);
//
//            String encodedFunction = FunctionEncoder.encode(function);
//
//            BigInteger gasPrice = BigInteger.valueOf(200000000000L);
//            BigInteger gasLimit = BigInteger.valueOf(5000000L);
//            //BigInteger nonce = null;
//            EthGetTransactionCount ethGetTransactionCount = web3.ethGetTransactionCount(
//                    "0xe57EB9F6f71B4f9F37e6aB730942851d067d402E", DefaultBlockParameterName.LATEST).send();
//            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
//
//
//            Transaction transaction = Transaction.createFunctionCallTransaction(
//                    "0xe57EB9F6f71B4f9F37e6aB730942851d067d402E", nonce, gasPrice, gasLimit, "0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718", encodedFunction);
//
//
//            RawTransaction rawTransaction  = RawTransaction.createTransaction(
//                    nonce, gasPrice, gasLimit, "0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718", encodedFunction);
//
//            //org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse =
//              //      web3.ethSendTransaction(transaction).sendAsync().get();
//
//            System.out.println("transaction: " + rawTransaction.getData());
//            //System.out.println("transaction: " + rawTransaction.);
//            System.out.println("transaction: " + rawTransaction.toString());
//            //System.out.println("transaction: " + rawTransaction.getData());
//
//
//            if(rawTransaction == null) {
//                System.out.println("raw transaction is null");
//            }
//
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
//            String hexValue = Numeric.toHexString(signedMessage);
//            EthSendTransaction rawTrasactionResponse = web3.ethSendRawTransaction(hexValue).send();
//
//
//            //org.web3j.protocol.core.methods.response.EthSendTransaction rawTrasactionResponse = web3.ethSendRawTransaction("0x" + rawTransaction.getData()).sendAsync().get();
//
//            //String transactionHash = transactionResponse.getTransactionHash();
//
//
//            String rawTransactionHash = rawTrasactionResponse.getTransactionHash();
//
//            System.out.println("Transaction hash: " + rawTransactionHash);
//            System.out.println("tr.toString(): " + rawTrasactionResponse.toString());
//            System.out.println("tr.getJsonrpc(): " + rawTrasactionResponse.getJsonrpc());
//            System.out.println("tr.getRawResponse(): " + rawTrasactionResponse.getRawResponse());
//            System.out.println("tr.getResult(): " + rawTrasactionResponse.getResult());
//            System.out.println("tr.hasError(): " + rawTrasactionResponse.hasError());
//            //System.out.println("tr.getError(): " + rawTrasactionResponse.getError().getMessage());
//
//
////            System.out.println("Transaction hash: " + transactionHash);
////            System.out.println("tr.toString(): " + transactionResponse.toString());
////            System.out.println("tr.getJsonrpc(): " + transactionResponse.getJsonrpc());
////            System.out.println("tr.getRawResponse(): " + transactionResponse.getRawResponse());
////            System.out.println("tr.getResult(): " + transactionResponse.getResult());
////            System.out.println("tr.hasError(): " + transactionResponse.hasError());
////            System.out.println("tr.getError(): " + transactionResponse.getError().getMessage());


                        //SWFHelper.contractSetHash(web3, credentials, "0xe57EB9F6f71B4f9F37e6aB730942851d067d402E", "0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718", secret.getTop());

                        //System.out.println("hash setting: " + secret.getTop());

                        //Thread.sleep(60000);


                        //System.out.println("hash returned: " + SWFHelper.contractGetHash(web3, "0xe57EB9F6f71B4f9F37e6aB730942851d067d402E", "0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718"));


                        // END OF MESSAROUND 1.5


                        // MESSAROUND 2

//            List<Type> inputParameters1 = new ArrayList<>();
//            List<TypeReference<?>> outputParameters1 = new ArrayList<>();
//
//
//            outputParameters1.add(new TypeReference<Bytes32>() {});
//
//            Function function1 = new Function("getHash",
//                    inputParameters1,
//                    outputParameters1);
//            String functionEncoder1 = FunctionEncoder.encode(function1);
//
//            EthCall response = web3.ethCall(Transaction.createEthCallTransaction("0xe57EB9F6f71B4f9F37e6aB730942851d067d402E","0xfbb8784d5980c19ce5c9e2d7fcc793b3176b1718", functionEncoder1), DefaultBlockParameterName.LATEST).sendAsync().get();
//
////            Type resault = someType.get(0);
////
////            String a = resault.toString();
//
//
//
//            List<Type> someType = FunctionReturnDecoder.decode(response.getValue(),function1.getOutputParameters());
//            Iterator<Type> it = someType.iterator();
//            Type resault = someType.get(0);
//            Bytes32 a = (Bytes32) resault;
//
//
//            System.out.println("Hello0: " + Numeric.toHexString(((Bytes32) resault).getValue()));
//            System.out.println("Hello0.0.1: " + Numeric.toHexString(a.getValue()) );
//            System.out.println("Hello0.1: " + response.toString());
//            System.out.println("Hello1: " + response.getValue());
//            System.out.println("Hello2: " + response.getResult());
//            System.out.println("Hello3: " + response.getRawResponse());

                        // END OF MESSAROUND 2

                        try {
                            // [[ RECEIVE: TRANSMISSION #5 ]]
                            reply = in.nextLine();
                            if(reply.equals("GO")){
                                mInstance.runOnUiThread (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    timetotalChrono.setBase(SystemClock.elapsedRealtime());
                                    timetotalChrono.start();
                                    simpleChronometer.setBase(SystemClock.elapsedRealtime());
                                    simpleChronometer.start();
                                    smartStatus.setText("Funded (0.072 ETH)");
                                }
                            }));
                        }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        reply = reply.replaceAll("\n", "");


                        if (!reply.equals("GO")) {
                            System.out.println("Nogo");
                            //Log.d("Nogo", "");
                            out.println("bye");
                            socket.close();
                            return;
                        }

                        long timestamp = SWFHelper.getTimestamp();
                        String line = "";


                        System.out.println("Start loop");
                        //Log.d("Start loop", "");


                        int chainsize = 60;
                        int subperiod_msec = 60000;

                        ArrayList<String> fullHashChain = SWFHelper.getFullHashChain(secret.getSeed(), 60);
                        Collections.reverse(fullHashChain);


                        ConnectionFragment.startDupset();

                        SWFHelper.nbSleep(10000);

                        double speed_min = 2.0;

                        int testdrive_periods = 2;

                        int count = 0;
                        for (int i = 0; i < chainsize * 2; i++) {

                            if (count >= chainsize) {
                                break;
                            }

                            if (ConnectionFragment.getSpeed() >= speed_min || i < testdrive_periods) {
                                out.println("ack:" + fullHashChain.get(count));
                                count++;
                                double totalChargeTemp = count * 0.0012;
                                if(flag == 1) {
                                    mInstance.runOnUiThread(new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                                            simpleChronometer.start();
                                        }
                                    }));
                                    flag = 0;
                                }
                                else {
                                    mInstance.runOnUiThread(new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            totalCharge.setText(valueOf(totalChargeTemp) + " ETH");
                                        }
                                    }));
                                }

                            } else {
                                out.println("TOOSLOW");
                            }

                            try {
                                reply = in.nextLine();

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            reply = reply.replaceAll("\n", "");

                            if (!reply.equals("GOTCHA")) {
                                flag = 1;
                                mInstance.runOnUiThread (new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
                                        simpleChronometer.stop();
                                    }
                                }));
                            }

                /* if(i == 6) {
                    SWFHelper.nbSleep(subperiod_msec * 2);
                } */

                            SWFHelper.nbSleep(subperiod_msec);
                        }

                        try {
                            Thread.sleep(1800000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        SWFHelper.contractRefund(web3, credentials, wallet_public, contract, secret.getTop());
                    }

                }catch(Exception e){
                    Log.d("Error : ",e.getMessage());
                }
            }
        });
        thread.start();

    }



}
