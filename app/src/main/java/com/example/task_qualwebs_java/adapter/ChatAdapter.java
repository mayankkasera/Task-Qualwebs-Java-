package com.example.task_qualwebs_java.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task_qualwebs_java.FilePath;
import com.example.task_qualwebs_java.R;
import com.example.task_qualwebs_java.model.Chat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewholder>{

    String reciver;
    String sender;
    Context context;
    List<Chat> list;

    public ChatAdapter(String reciver, String sender, Context context, List<Chat> list) {
        this.reciver = reciver;
        this.sender = sender;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message, parent, false);
        return new ChatViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewholder holder, int position) {
        final Chat model = list.get(position);
        Log.i("dscbmsdn", "onBindViewHolder: "+model.toString());


        if (model.getType().equals("send")){
            if (model.getStatus().equals("read")){
                holder.blue.setVisibility(View.VISIBLE);
                holder.grey.setVisibility(View.GONE);
            }
            else {
                holder.grey.setVisibility(View.VISIBLE);
                holder.blue.setVisibility(View.GONE);
            }

            if(model.getMessage_type().equals("image")){
                holder.sendImgLayout.setVisibility(View.VISIBLE);
                holder.reciveImgLayout.setVisibility(View.GONE);
                holder.schat.setVisibility(View.GONE);
                holder.chat.setVisibility(View.GONE);

                if(model.getUpload_status().equals("uploading")){
                    holder.sendImgPro.setVisibility(View.VISIBLE);
                    holder.sendImg.setImageURI(null);
                    String selectedFilePath = FilePath.getPath(context, Uri.parse(model.getUri()));
                    Bitmap bmImg = BitmapFactory.decodeFile(selectedFilePath);
                    holder.sendImg.setImageBitmap(bmImg);

                }
                else {
                    holder.sendImg.setImageURI(null);
                    if(uriValidate(model.getUri())){
                        String selectedFilePath = FilePath.getPath(context, Uri.parse(model.getUri()));
                        Bitmap bmImg = BitmapFactory.decodeFile(selectedFilePath);
                        holder.sendImg.setImageBitmap(bmImg);
                        holder.sendImgPro.setVisibility(View.GONE);
                    }
                    else{
                        Picasso.get().load(model.getUrl()).into(new Target() {
                            @Override
                            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                                holder.sendImg.setImageBitmap(bitmap);
                                holder.sendImgPro.setVisibility(View.GONE);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }



                }
            }
            else {
                holder.schat.setVisibility(View.VISIBLE);
                holder.chat.setVisibility(View.GONE);

                holder.sendImgLayout.setVisibility(View.GONE);
                holder.reciveImgLayout.setVisibility(View.VISIBLE);
            }


            holder.chat.setBackgroundColor(Color.WHITE);
            holder.schat.setText(model.getMessage());
            holder.timestamp.setText(getDate(model.getTime()));
            holder.flayout.setVisibility(View.GONE);
            holder.slayout.setVisibility(View.VISIBLE);


        }
        else{
            holder.chat.setBackgroundColor(Color.WHITE);
            holder.chat.setText(model.getMessage());
            holder.stime.setText(getDate(model.getTime()));
            holder.slayout.setVisibility(View.GONE);
            holder.flayout.setVisibility(View.VISIBLE);

            if(model.getMessage_type().equals("image")){
                holder.sendImgLayout.setVisibility(View.GONE);
                holder.reciveImgLayout.setVisibility(View.VISIBLE);
                holder.schat.setVisibility(View.GONE);
                holder.chat.setVisibility(View.GONE);




                Picasso.get().load(model.getUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        holder.reciveImg.setImageBitmap(bitmap);
                        holder.reciveImgPro.setVisibility(View.GONE);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


            }
            else {
                holder.schat.setVisibility(View.GONE);
                holder.chat.setVisibility(View.VISIBLE);

                holder.sendImgLayout.setVisibility(View.VISIBLE);
                holder.reciveImgLayout.setVisibility(View.GONE);
            }
        }






        if (model.getType().equals("recive") && model.getStatus().equals("unread")) {
            final Map<String, Object> map = new HashMap<>();
            map.put("status","read");

            FirebaseDatabase.getInstance().getReference().child("Message")
                    .child(reciver).child(sender)
                    .child(model.getReciver())
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference().child("Message")
                                    .child(sender).child(reciver)
                                    .child(model.getSender())
                                    .updateChildren(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    }) ;
                        }
                    });


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ChatViewholder extends RecyclerView.ViewHolder {

        private TextView chat, schat;
        private LinearLayout flayout;
        private RelativeLayout slayout;
        private TextView timestamp,stime;
        private ImageView grey,blue,sendImg,reciveImg;
        private ProgressBar sendImgPro,reciveImgPro;
        private CardView sendImgLayout,reciveImgLayout;


        public ChatViewholder(@NonNull View itemView) {
            super(itemView);
            chat = itemView.findViewById(R.id.chat);
            schat = itemView.findViewById(R.id.schat);
            flayout = itemView.findViewById(R.id.flayout);
            slayout = itemView.findViewById(R.id.slayout);
            timestamp = itemView.findViewById(R.id.timestamp);
            stime= itemView.findViewById(R.id.stimestamp);
            grey= itemView.findViewById(R.id.grey);
            blue= itemView.findViewById(R.id.blue);

            sendImgLayout = itemView.findViewById(R.id.sendImgLayout);
            reciveImgLayout = itemView.findViewById(R.id.reciveImgLayout);

            sendImgPro = itemView.findViewById(R.id.sendImgPro);
            reciveImgPro = itemView.findViewById(R.id.reciveImgPro);

            sendImg = itemView.findViewById(R.id.sendImage);
            reciveImg = itemView.findViewById(R.id.reciveImage);


        }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        //String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        //return date;
        String ctime = DateFormat.format("HH:mm", cal).toString();
        return ctime;
    }

    private boolean uriValidate(String uri){
        try {
            String selectedFilePath = FilePath.getPath(context, Uri.parse(uri));
            final File file = new File(selectedFilePath);
            if (file.exists())
                return true;
            else
                return false;

        }
        catch (Exception e)
        {
            return false;
        }
    }
}
