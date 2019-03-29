package com.thinklab.smartwifi;
import android.os.Environment;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import java.io.File;
import java.math.BigDecimal;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Wallet {
    // Create new wallet
    public String createWallet() throws Exception {
        String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath();
        String fileName = WalletUtils.generateLightNewWalletFile("password", new File(path));
        return path + "/" + fileName;
    }

    /* Load the new wallet file once it is created
     *  Replace the File path with newly created wallet file path
     */
    public Credentials loadCredentials(String password, String fileName) throws Exception {

        Credentials credentials = WalletUtils.loadCredentials(
                password,
                fileName);
        return credentials;
    }
    public String sendTransaction(Web3j web3, Credentials credentials) throws Exception {
        TransactionReceipt transferReceipt = Transfer.sendFunds(web3, credentials,
                "0xF88dF2c638ec22B422Ab519f07636a9e47f470df",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        return transferReceipt.getTransactionHash();
    }
}

