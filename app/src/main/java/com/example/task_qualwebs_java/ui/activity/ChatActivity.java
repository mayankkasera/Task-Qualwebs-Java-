package com.example.task_qualwebs_java.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.task_qualwebs_java.R;

public class ChatActivity extends AppCompatActivity {

    Button User1,User2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        User1 = findViewById(R.id.chatAsUser1);
        User2 = findViewById(R.id.chatAsUser2);

        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, MessageActivity.class);
                i.putExtra("data","User1");
                startActivity(i);
            }
        });

        User2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, MessageActivity.class);
                i.putExtra("data","User2");
                startActivity(i);
            }
        });





    }
}
