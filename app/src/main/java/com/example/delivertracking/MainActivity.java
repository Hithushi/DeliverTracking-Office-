package com.example.delivertracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DecoratedBarcodeView dbvScanner;
    ArrayList<String> arrayList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbvScanner = (DecoratedBarcodeView) findViewById(R.id.dbv_barcode);
        requestPermissions();

        dbvScanner.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                updateText(result.getText());
                beepSound();
            }

            @Override
            public void possibleResultPoints(List<com.google.zxing.ResultPoint> resultPoints) {
                BarcodeCallback.super.possibleResultPoints(resultPoints);
            }
        });
    }

    private void beepSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateText(String text) {
        arrayList.clear();
        arrayList.add(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeScanner();
    }

    protected void resumeScanner() {
        boolean isScanDone = false;
        if (!dbvScanner.isActivated())
            dbvScanner.resume();
        Log.d("peeyush-pause","paused: false");
    }

    protected void pauseScanner() {
        dbvScanner.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseScanner();
    }

    void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length < 1) {
            requestPermissions();
        } else {
            dbvScanner.resume();
        }
    }
}