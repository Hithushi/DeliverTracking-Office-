package com.example.delivertracking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MailbagDeliver extends AppCompatActivity {

    TextView city, address, locationT;
    TextView date, time;
    EditText remark;
    Button scan_btn, submit_btn;
    TextView barcodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbag_deliver);
        getWindow().setStatusBarColor(ContextCompat.getColor(MailbagDeliver.this,R.color.dark_blue));

        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        locationT = findViewById(R.id.locationText);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        remark = findViewById(R.id.remark);
        barcodeResult = findViewById(R.id.barcodeResult);
        scan_btn = findViewById(R.id.scan_btn);
        submit_btn = findViewById(R.id.submit_btn);

        scan_btn.setOnClickListener(v -> startScanner());
    }

    private void startScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Please scan Barcode", Toast.LENGTH_SHORT).show();
            } else {
                updateText(result.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateText(String scanCode) {
        barcodeResult.setText(scanCode);
    }
}