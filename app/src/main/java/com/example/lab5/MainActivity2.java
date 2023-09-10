package com.example.lab5;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity2 extends AppCompatActivity {
    EditText nameInput;
    EditText phoneInput;
    ImageButton btn2;
    EditText emailInput;
    Intent intent;
    ArrayList<String> set;
    SharedPreferences preferences;
    Set<String> nameSet;
    boolean validNum;
    boolean validName;
    boolean valid = true;
    boolean validn = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn2  = findViewById(R.id.addBtn);
        btn2.setEnabled(true);
        preferences = this.getSharedPreferences("contact", Context.MODE_PRIVATE);
        emailInput   = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        nameInput   = findViewById(R.id.nameInput);
        getSet();

                phoneInput.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    public void afterTextChanged(Editable s) {
                        count();
                    }
                });
                nameInput.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        checkName();

                    }
                    public void afterTextChanged(Editable s) {
                        checkName();
                    }
                });
                nameInput.setText(intent.getStringExtra("name"));
                phoneInput.setText(intent.getStringExtra("number"));
                try {
                    emailInput.setText(intent.getStringExtra("email").replace("  ",""));
                }catch (Exception e){
                    //
                }
            }


    public void submit(View v) {
        String p = phoneInput.getText().toString().replace(" ", "");
        String n = nameInput.getText().toString();
        String em = emailInput.getText().toString();
        int index = intent.getIntExtra("index",0);
        boolean alreadyFoundName= false;

        if(intent.getStringExtra("editable").equals("true")){
            String stri = nameInput.getText().toString();

            int count = 0;
            String s = "";
            if(!stri.equals(intent.getStringExtra("name")))
                for (String f: set
                     ) {
                    String nameStr = f.split("-")[0];

                    if(stri.equals(nameStr)){
                        alreadyFoundName = true;
                    }
                }
            if(alreadyFoundName){
                for (int z=0; z< set.size();z++){
                    if(z!=index){
                        String nameStr = set.get(z).split("-")[0];
                        if (nameStr.startsWith(stri)) {
                            Log.d(TAG, "submit: " + stri);

                            count++;
                            s = stri + "(" + count +  ")";
                            validn = false;
                        }
                    }
                }
                if(!validn){
                    stri=s;
                }
            }

            if(!p.equals(intent.getStringExtra("number"))){
                for (String str : set
                ) {
                    String tempStr = str.split("-")[1];
                    if (tempStr.equals(p)) {
                        Toast.makeText(this, "Number Already Exist", Toast.LENGTH_LONG).show();
                        valid = false;
                    }
                }
            }
            if(valid){
                if (em.equals("")) {
                    em = "  ";
                }
                String fullInfo = stri + "-" + p + "-" + em;
                set.set(index,fullInfo);
                nameSet = new HashSet<>(set);
                SharedPreferences.Editor e = preferences.edit();
                e.putStringSet("ContactList", nameSet);
                e.apply();
                finish();
            }else {
                valid = true;
            }
        }else {
             runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    addContact();
                }
            });
        }
    }

    public  void addContact(){
        String p = phoneInput.getText().toString().replace(" ", "");
        String n = nameInput.getText().toString();
        String em = emailInput.getText().toString();
        SharedPreferences.Editor e = preferences.edit();
        int count = 0;

        String s = "";
        for (String str : set
        ) {
            String tempStr = str.split("-")[1];
            String nameStr = str.split("-")[0];
            if (tempStr.equals(p)) {
                Toast.makeText(this, "Number Already Exist", Toast.LENGTH_LONG).show();
                valid = false;
            }
            if (nameStr.split("\\(")[0].equals(n)) {
                count++;
                s = n + "(" + count + ")";
                validn = false;
            }
        }
        if (valid) {
            if (!validn) {
                n = s;
            }
            if (em.equals("")) {
                em = "  ";
            }
            String fullInfo = n + "-" + p + "-" + em;
            set.add(fullInfo);
            nameSet = new LinkedHashSet<>(set);
            e.putStringSet("ContactList", nameSet);
            e.apply();
            finish();
        } else {
            valid = true;
        }
    }

    public void getSet() {
        intent = getIntent();
        nameInput.setText(intent.getStringExtra("name"));
        phoneInput.setText(intent.getStringExtra("number"));
        emailInput.setText(intent.getStringExtra("email"));
        nameSet = preferences.getStringSet("tempList", new HashSet<>());
        set = new ArrayList<>(nameSet);
    }
    public void count() {
        try {
            String k = phoneInput.getText().toString().replace(" ", "");
            Log.d("TAG", k);
            int count = k.length();
            validNum = count == 8;
            if(count == 8 && validName){
                btn2.setEnabled(true);

            }else {
                btn2.setEnabled(false);

            }
        } catch (Exception e) {
            //
        }
    }
    public void checkName() {
        String s = nameInput.getText().toString().replace(" ", "");
        int z = s.length();
        validName = z >= 3;

        if(z >= 3 && validNum){
            btn2.setEnabled(true);

        }else {
            btn2.setEnabled(false);
        }

    }
}