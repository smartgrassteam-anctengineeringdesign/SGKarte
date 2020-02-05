package com.example.kamadayuji.smartglass_systemflow;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.kamadayuji.smartglass_systemflow.database.DBAESEncryption;


public class debugmode extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debugmode);

        try {
            String password = "test12345";
            byte[] data = "plaintext11223344556677889900".getBytes("UTF-8");
            DBAESEncryption.EncryptedData encData = DBAESEncryption.encrypt(password, data);
            byte[] decryptedData =  DBAESEncryption.decrypt(password, encData.salt, encData.iv, encData.encryptedData);
            String decDataAsString = new String(decryptedData, "UTF-8");
            Toast.makeText(this, decDataAsString, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
