package com.ncrdesarrollo.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ncrdesarrollo.popularmovies.DetalleActivity;
import com.ncrdesarrollo.popularmovies.R;
import com.ncrdesarrollo.popularmovies.models.MovieModel;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.myViewHolder> {

    private Context context;
    private List<MovieModel> data;

    public MovieAdapter(Context context, List<MovieModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_movie, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        //holder.title.setText(data.get(position).getTitle());

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500"+data.get(position).getPoster_path())
                .into(holder.img);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetalleActivity.class);
                intent.putExtra("img",data.get(position).getPoster_path());
                intent.putExtra("title",data.get(position).getTitle());
                intent.putExtra("overview",data.get(position).getOverview() );
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
        View view;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //title = itemView.findViewById(R.id.tv_titulo);
            img = itemView.findViewById(R.id.image);
            view = itemView;
        }
    }
}
