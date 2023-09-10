package com.example.lab5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab5.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ListView contactsList;
    Set<String> nameSet;
    EditText search;
    ArrayList<String> names;
    boolean clicked = true;
    ArrayAdapter<String> adapter;
    SharedPreferences preferences;
    RelativeLayout relativeLayout;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = this.getSharedPreferences("contact", Context.MODE_PRIVATE);
        start();

        relativeLayout = findViewById(R.id.relativel);
        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        relativeLayout.setOnClickListener(v -> {
            search.setVisibility(View.GONE);
            clicked = true;
        });
        setSupportActionBar(binding.toolbar);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                names.clear();
                String[] arr = nameSet.toArray(new String[0]);
                for (int i  = 0; i< nameSet.size();i++ ) {
                    String num = arr[i].split("-")[1].toLowerCase();
                    String name = arr[i].split("-")[0].toLowerCase();
                    String w = s.toString().replace(" ", "");
                    if (name.contains(s.toString().toLowerCase()) || num.startsWith(w)) {
                        String z = arr[i].split("-")[0];
                        names.add(z);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        contactsList.setOnItemClickListener((parent, view, i, id) -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
            ArrayList<String> nameList = new ArrayList<>(nameSet);
            int index = i;
            String name = nameList.get(i).split("-")[0];
            String number = nameList.get(i).split("-")[1];
            String email = nameList.get(i).split("-")[2];
            for (int x = 0 ; x< nameList.size();x++)
            {
                if(nameList.get(x).split("-")[0].equals(names.get(i))){
                     index = x;
                     name = nameList.get(x).split("-")[0];
                     number = nameList.get(x).split("-")[1];
                     email = nameList.get(x).split("-")[2];
                }
            }

            intent.putExtra("name", name);
            intent.putExtra("number", number);
            intent.putExtra("email",email);
            intent.putExtra("index", index);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();
        if (id == R.id.addContactItem) {
            Intent intent = new Intent(this, MainActivity2.class);
            intent.putExtra("editable","false");
            startActivity(intent);
        }
        if (id == R.id.searchItem) {
            search = findViewById(R.id.search);
            if (clicked) {
                search.setVisibility(View.VISIBLE);
                clicked = false;
            } else {
                search.setVisibility(View.GONE);
                search.setText("");
                clicked = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void start() {
        preferences = this.getSharedPreferences("contact", Context.MODE_PRIVATE);
        nameSet = preferences.getStringSet("ContactList", new HashSet<>());
        SharedPreferences.Editor p = preferences.edit();
        p.putStringSet("tempList", nameSet);
        p.apply();
        names = new ArrayList<>();
        for (String a : nameSet
        ) {
            String tempStr = a.split("-")[0];
            names.add(tempStr);
        }
        contactsList = findViewById(R.id.contactsList);
        adapter = new ArrayAdapter<>(this, androidx.transition.R.layout.support_simple_spinner_dropdown_item,
                names);
        contactsList.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
}