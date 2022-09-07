package com.example.nytimesnewsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class adaptter extends  RecyclerView.Adapter<adaptter.ViewHolder> {


    private ArrayList<liststructure> data;
    public static int posit;

    public static Bitmap getBitmapFromURL(String src) {
        Bitmap mIcon_val = null;
        try {
            URL newurl = new URL(src);
            mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            return mIcon_val;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mIcon_val ;
    }

    public adaptter(ArrayList<liststructure> data){

        this.data=data;
    }


    @NonNull
    @Override
    public adaptter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptter.ViewHolder holder, int position) {
        String title = data.get(position).getA();
        String lastt= data.get(position).getB();
        String date = data.get(position).getC();
        String imgurl = data.get(position).getD();
        holder.setData(title,lastt,date,imgurl);



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView a,b,e;
        private ImageView c;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            c=itemView.findViewById(R.id.imageView15);
            a=itemView.findViewById(R.id.textView8);
            b=itemView.findViewById(R.id.lastmessage);
            e=itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent net = new Intent(v.getContext(), articlepage.class);
                    posit= getAdapterPosition(); net.putExtra("nameofselected",data.get(posit).getlink());
                    Log.i("gfdfgfdhgfhgjf",data.get(posit).getlink());
                    v.getContext().startActivity(net);
                }
            });

        }
        public void setData(String desc,String last, String date,String image) {

            a.setText(desc);
            b.setText(last);
            e.setText(date);

            // using picasso to set image

            Picasso.with(itemView.getContext())
                    .load(image)
                    .into(c);

        }
    }
}
