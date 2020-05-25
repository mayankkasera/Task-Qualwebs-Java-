package com.example.task_qualwebs_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task_qualwebs_java.adapter.ChatAdapter;
import com.example.task_qualwebs_java.model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 100;
    private DatabaseReference message;
    private String sender,reciver;
    private ChatAdapter ada;
    private List<Chat> list = new ArrayList<>();
    private List<String> mKeys = new ArrayList<>();
    private RecyclerView recyclerview;
    private ImageView send,attachment;
    private EditText messageEdt;
    private final int PERMISSION_ALL = 2534;

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

        final String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!hasPermissions(MessageActivity.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(MessageActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                else {
                    Intent image = new Intent();
                    image.setType("image/*");
                    image.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(image,"Select Image"),GALLERY_PICK);

                }

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
        map.put("message_type","text");

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
                map.put("message_type","text");

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
        attachment = findViewById(R.id.attachment);

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

        ada = new ChatAdapter(reciver,sender,this,list);
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSION_ALL:{
                if (grantResults.length > 0) {
                    boolean flag = true;
                    for (int i = 0 ; i < permissionsList.length ; i++ ) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            flag = false;
                        }
                    }

                    if(flag){
                        Intent image = new Intent();
                        image.setType("image/*");
                        image.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(image,"Select Image"),GALLERY_PICK);
                    }
                    // Show permissionsDenied

                }
                return;
            }
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK) // Check There Is Everuthing Fine Or Not
        {
            final Uri imguri = data.getData(); // Getting Selected Image In Uri Form
            sendImg(imguri);
        }
    }

    private void sendImg(final Uri imguri) {
        final DatabaseReference senderRef = message.child(sender).child(reciver).push();
        final DatabaseReference reciverRef = message.child(reciver).child(sender).push();


        Map<String, Object> map  = new HashMap();
        map.put("message","bjhbjb");
        map.put("time",ServerValue.TIMESTAMP);
        map.put("status","unread");
        map.put("reciver",reciverRef.getKey());
        map.put("sender",senderRef.getKey());
        map.put("type","send");
        map.put("message_type","image");
        map.put("upload_status","uploading");
        map.put("uri",imguri.toString());


        senderRef.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                final StorageReference Image_message = FirebaseStorage.getInstance().getReference()
                        .child("Image_message").child(senderRef.getKey()+".png");




                Image_message.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Image_message.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {
                                Log.i("sdfcbsdm", "onSuccess: "+uri.toString());

                                Map<String, Object> map  = new HashMap();
                                map.put("upload_status","uploaded");
                                map.put("url",uri.toString());

                                senderRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Map<String, Object> map  = new HashMap();
                                        map.put("message","sdncbsd");
                                        map.put("time",ServerValue.TIMESTAMP);
                                        map.put("status","unread");
                                        map.put("reciver",senderRef.getKey());
                                        map.put("sender",reciverRef.getKey());
                                        map.put("type","recive");
                                        map.put("message_type","image");
                                        map.put("url",uri.toString());

                                        reciverRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("sdfcbsdm", "onSuccess: "+e.toString());
                    }
                });
            }
        });
    }

}
