package com.example.lab5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity3 extends AppCompatActivity {
    TextView nameText;
    TextView numberText;
    TextView emailText;
    int index;
    String name;
    String email;
    String number;
    ImageButton deleteBtn;
    ArrayList<String> list;
    Intent intent;
    Set<String> nameSet;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        intent = getIntent();
        deleteBtn = findViewById(R.id.deleteBtn);
        nameText = findViewById(R.id.nameText);
        numberText = findViewById(R.id.numberText);
        emailText = findViewById(R.id.emailAddress);

        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        email = intent.getStringExtra("email");

        nameText.setText(name);
        numberText.setText(number);
        if (email.equals("  ")) {
            emailText.setText("No email Found");
        } else {
            emailText.setText(email);
        }
    }

    public void back(View v) {
        finish();
    }

    public void delete(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to remove contact ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deletee();
                }).setNegativeButton("No", (d, i) -> {
                    d.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deletee() {
        SharedPreferences preferences = this.getSharedPreferences("contact", Context.MODE_PRIVATE);
        nameSet = preferences.getStringSet("ContactList", new HashSet<>());
        list = new ArrayList<>(nameSet);
       index = intent.getIntExtra("index", 0);
        list.remove(index);
        nameSet = new HashSet<>(list);
        for (String str : nameSet
        ) {
            Log.d("TAG", "delete: " + str);
        }
        SharedPreferences.Editor e = preferences.edit();
        e.putStringSet("ContactList", nameSet);
        e.apply();
        finish();

    }
    public void call(View v){
        Uri n = Uri.parse("tel:"+ number);
        Intent intent = new Intent(Intent.ACTION_CALL,n);
        startActivity(intent);
      //  Intent i = new Intent()




    }

    public void edit(View v) {
        index = intent.getIntExtra("index", 0);
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("name", name);
        intent.putExtra("number", number);
        intent.putExtra("email", email);
        intent.putExtra("editable","true");
        intent.putExtra("index",index);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}