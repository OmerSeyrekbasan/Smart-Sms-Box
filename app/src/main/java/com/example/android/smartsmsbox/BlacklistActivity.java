package com.example.android.smartsmsbox;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class BlacklistActivity extends AppCompatActivity implements ContactAdapter.ItemClickListener {
    private static ContactAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton button;
    private ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = (ArrayList<String>) FileManager.readStringFile("Blacklist", this);

        setContentView(R.layout.activity_blacklist);
        recyclerView = findViewById(R.id.blackList_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        button = findViewById(R.id.add_blacklist);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BlacklistActivity.this, NewBlacklistActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        FileManager.deleteElem(list.get(position),"Blacklist",this);
        list = (ArrayList<String>) FileManager.readStringFile("Blacklist", this);
        adapter.swapDataSet(list);
        Toast.makeText(BlacklistActivity.this, "Element Removed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = (ArrayList<String>) FileManager.readStringFile("Blacklist", this);
        adapter.swapDataSet(list);
    }

}
