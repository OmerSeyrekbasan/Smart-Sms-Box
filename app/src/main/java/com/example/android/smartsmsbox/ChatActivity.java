package com.example.android.smartsmsbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.ItemClickListener {
    private static ChatAdapter adapter;
    private ArrayList<SMS> smsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        smsList = (ArrayList<SMS>) getIntent().getSerializableExtra("SMSLIST");
        RecyclerView recyclerView = findViewById(R.id.chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(this, smsList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position)
                + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    public static void notifyDataAdded(SMS s) {
        adapter.swapDataSet(s);
    }

}
