package com.thinklab.smartwifi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.PrintWriter;
import android.support.v4.app.FragmentManager;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;


public class SWFClient {

    public static void run2(Activity activity) throws Exception {
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
                               activity.runOnUiThread (new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       ConnectionFragment.C1.setConnectionClock(true);
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
                                System.out.println("QUIT");
                                out.println("bye");
                                socket.close();
                                return;
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
