package com.example.ciclotm;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class generalRecycleViewAdapter extends RecyclerView.Adapter<generalRecycleViewAdapter.MyViewHolder>{
    Context context;
    private StorageReference storageReference;

    private ArrayList<generalPost> postsList;

    public generalRecycleViewAdapter(Context context, ArrayList<generalPost> postsList){
        this.context=context;
        this.postsList=postsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView data;
        ImageView user_photo;
        public MyViewHolder(final View view){
            super(view);
            title=(TextView) view.findViewById(R.id.upperTextView);
            content=(TextView) view.findViewById(R.id.lowerTextView);
            data= (TextView) view.findViewById(R.id.middleGeneralTextView);
            user_photo=(ImageView) view.findViewById(R.id.user_photo);
        }
    }
    @NonNull
    @Override
    public generalRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_general_card_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull generalRecycleViewAdapter.MyViewHolder holder, int position) {
        String title= postsList.get(position).getTitle();
        holder.title.setText(title);
        String content= postsList.get(position).getContent();
        holder.content.setText(content);
        Date date=postsList.get(position).getDate();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm  dd/MM/yyyy", Locale.getDefault());
        String output=df.format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(postsList.get(position).getDate());
        String data= String.valueOf(calendar.get(Calendar.HOUR))+":"+String.valueOf(calendar.get(Calendar.MINUTE))+" "+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+
                String.valueOf(calendar.get(Calendar.MONTH))+String.valueOf(calendar.get(Calendar.YEAR));
        holder.data.setText(output);

        storageReference = FirebaseStorage.getInstance().getReference().child("UsersProfilePicture/person.png");
        File localFile= null;
        try {
            localFile = File.createTempFile("tempFile","png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File finalLocalFile = localFile;
        storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                holder.user_photo.setImageBitmap(bitmap);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


}
