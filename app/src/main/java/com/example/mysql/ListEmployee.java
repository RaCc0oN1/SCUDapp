package com.example.mysql;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListEmployee extends AppCompatActivity {

    private Button returnBtn;

    private int maxID;

    ArrayList<User> listUsers = new ArrayList<>();
    private getIDforAll GetId;
    private getInfo Getinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_employee);
        returnBtn = findViewById(R.id.button4);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEmplList();
            }
        });
        ListView listView = (ListView) findViewById(R.id.userList);
        getAllInfo();
       customAdapter adapter = new customAdapter(this, listUsers);
        listView.setAdapter(adapter);
    }
    private void openEmplList() {
        finish();
    }

    public void getMaxId() {
        GetId = new getIDforAll();
        GetId.start("1");
        try {
            GetId.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0 ", ie.getMessage());
        }
        maxID = Integer.parseInt(GetId.getIde());
        // Toast.makeText(this, maxID, Toast.LENGTH_LONG).show();
    }

    public void getAllInfo() {
        Getinfo = new getInfo();
        getMaxId();
        for (int i = 1; i < maxID + 1; i++) {
            listUsers.add(new User(i, Getinfo.resname(), Getinfo.ressurname(), Getinfo.resmiddlename(), Getinfo.resbtnstate(), Getinfo.getRfid_id()));
        }
        try {
            Getinfo.join();
        } catch (InterruptedException ie) {
            Log.e("pass 0 ", ie.getMessage());
        }


    }


}
