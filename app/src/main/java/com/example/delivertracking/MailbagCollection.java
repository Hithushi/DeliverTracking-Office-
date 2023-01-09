package com.example.delivertracking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.delivertracking.Database.DBConnection;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MailbagCollection extends AppCompatActivity {
    Connection conn = null;

    TextView city, locationT, date, time, barcodeResult, longitude, latitude;
    EditText remark, address;
    Button scan_btn, submit_btn;
    Spinner spinner;
    String[] status = {"Mailbag Received","Unable to Collect Mailbag"};

    FusedLocationProviderClient fusedLocationProviderClient;

    private final static int REQUEST_CODE = 100;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbag_collection);

        barcodeResult = findViewById(R.id.barcodeResult);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        locationT = findViewById(R.id.locationText);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        remark = findViewById(R.id.remark);
        spinner = findViewById(R.id.statusSpinner);
        scan_btn = findViewById(R.id.scan_btn);
        submit_btn = findViewById(R.id.submit_btn);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        scan_btn.setOnClickListener(v -> {
            startScanner();
            getLastLocation();
            date.setText(getCurrentDate());
            time.setText(getCurrentTime());
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MailbagCollection.this,R.layout.deliver_status_item,status);
        adapter.setDropDownViewResource(R.layout.deliver_status_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    getLastLocation();
                    date.setText(getCurrentDate());
                    time.setText(getCurrentTime());
                    barcodeResult.setText(null);
                    scan_btn.setEnabled(false);
                } else {
                    scan_btn.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        date.addTextChangedListener(addressTextWatcher);

        submit_btn.setOnClickListener(v -> {
            try {
                DBConnection connect = new DBConnection();
                conn = connect.createConnection();
                String text = spinner.getSelectedItem().toString();
                if (conn != null) {
                    Toast.makeText(this,"Connection Established", Toast.LENGTH_LONG).show();
                    String sqlInsert = "Insert into TBL_Mailbag_Collection values ('"+barcodeResult.getText().toString()+"','"+text+"','"+address.getText().toString()+"','"+city.getText().toString()+"','"+date.getText().toString()+"','"+time.getText().toString()+"','"+locationT.getText().toString()+"','"+longitude.getText().toString()+"','"+latitude.getText().toString()+"','"+remark.getText().toString()+"')";
                    Statement statement = conn.createStatement();
                    statement.executeQuery(sqlInsert);
                } else {
                    Toast.makeText(this, "Connection not established", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("Error: ",e.getMessage());
            }
            barcodeResult.setText(null);
            address.getText().clear();
            city.setText(null);
            locationT.setText(null);
            time.setText(null);
            date.setText(null);
            longitude.setText(null);
            latitude.setText(null);
            remark.getText().clear();
            spinner.setAdapter(adapter);
        });
    }

    private final TextWatcher addressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String addressInput = date.getText().toString().trim();
            submit_btn.setEnabled(!addressInput.isEmpty());
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(MailbagCollection.this, Locale.getDefault());
                            List<Address> addresses;
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                latitude.setText(""+addresses.get(0).getLatitude());
                                longitude.setText(""+addresses.get(0).getLongitude());
                                address.setText(addresses.get(0).getAddressLine(0));
                                locationT.setText(addresses.get(0).getAddressLine(0));
                                city.setText(addresses.get(0).getLocality());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MailbagCollection.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null) {
            if (result.getContents() == null){
                Toast.makeText(this, "Please scan Barcode", Toast.LENGTH_SHORT).show();
            } else {
                barcodeResult.setText(result.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}