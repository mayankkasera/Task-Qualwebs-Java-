package com.example.task_qualwebs_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.task_qualwebs_java.adapter.ChatAdapter;
import com.example.task_qualwebs_java.model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private DatabaseReference message;
    private String sender,reciver;
    private ChatAdapter ada;
    private List<Chat> list = new ArrayList<>();
    private List<String> mKeys = new ArrayList<>();
    private RecyclerView recyclerview;
    private ImageView send;
    private EditText messageEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        init();

        setUpRecyclerView();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEdt.getText().toString();
                messageEdt.setText("");
                sendMsg(text);
            }
        });



    }

    private void sendMsg(final String text) {

        final DatabaseReference senderRef = message.child(sender).child(reciver).push();
        final DatabaseReference reciverRef = message.child(reciver).child(sender).push();

        Map<String, Object> map  = new HashMap();
        map.put("message",text);
        map.put("time",ServerValue.TIMESTAMP);
        map.put("status","unread");
        map.put("reciver",reciverRef.getKey());
        map.put("sender",senderRef.getKey());
        map.put("type","send");

        senderRef.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Map<String, Object> map  = new HashMap();
                map.put("message",text);
                map.put("time",ServerValue.TIMESTAMP);
                map.put("status","unread");
                map.put("reciver",senderRef.getKey());
                map.put("sender",reciverRef.getKey());
                map.put("type","recive");

                reciverRef.setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

            }
        });


    }


    private void init() {

        recyclerview = findViewById(R.id.recyclerview);
        send = findViewById(R.id.send);
        messageEdt = findViewById(R.id.messageEdt);

        message = FirebaseDatabase.getInstance().getReference().child("Message");
        message.keepSynced(true);

        if(getIntent().getStringExtra("data").equals("User1")){
            sender = "User1";
            reciver = "User2";
        }
        else{
            sender = "User2";
            reciver = "User1";
        }
    }

    private void setUpRecyclerView() {

        ada = new ChatAdapter(reciver,sender,list);
        recyclerview.setAdapter(ada);

        message.child(sender).child(reciver)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        mKeys.add(dataSnapshot.getKey().toString());
                        list.add(chat);
                        ada.notifyDataSetChanged();
                        recyclerview.scrollToPosition(list.size() - 1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Chat chatModel = dataSnapshot.getValue(Chat.class);
                        String key = dataSnapshot.getKey();
                        int index = mKeys.indexOf(key);
                        list.set(index, chatModel);
                        ada.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

}
