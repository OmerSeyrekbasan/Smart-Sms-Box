package com.example.android.smartsmsbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewBlacklistActivity extends AppCompatActivity {
    private Button button;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_blacklist);

        button = (Button) findViewById(R.id.submit);
        text = (EditText) findViewById(R.id.contact_address);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = text.getText().toString();
                FileManager.writeFile(s,"Blacklist",NewBlacklistActivity.this);
                NewBlacklistActivity.this.finish();
            }
        });

    }
}
