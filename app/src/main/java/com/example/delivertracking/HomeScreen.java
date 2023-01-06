package com.example.delivertracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    public CardView MailbagD, MailbagC, MailbagDBulk, MailbagCBulk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeScreen.this, R.color.dark_blue));

        MailbagD = findViewById(R.id.mailbag_deliver);
        MailbagC = findViewById(R.id.mailbag_collection);
        MailbagDBulk = findViewById(R.id.mailbag_deliverBulk);
        MailbagCBulk = findViewById(R.id.mailbag_collectionBulk);

        MailbagD.setOnClickListener(this);
        MailbagC.setOnClickListener(this);
        MailbagCBulk.setOnClickListener(this);
        MailbagDBulk.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.mailbag_deliver:
                intent = new Intent(this, MailbagDeliver.class);
                startActivity(intent);
                break;
            case R.id.mailbag_collection:
                intent = new Intent(this, MailbagCollection.class);
                startActivity(intent);
                break;
            case R.id.mailbag_deliverBulk:
                intent = new Intent(this, DeliverBulk.class);
                startActivity(intent);
                break;
            case R.id.mailbag_collectionBulk:
                intent = new Intent(this, CollectionBulk.class);
                startActivity(intent);
                break;
        }
    }
}