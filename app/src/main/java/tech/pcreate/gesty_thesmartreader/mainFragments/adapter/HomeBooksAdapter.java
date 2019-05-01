/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.mainFragments.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import tech.pcreate.gesty_thesmartreader.R;
import tech.pcreate.gesty_thesmartreader.mainFragments.model.HomeBookCover;

public class HomeBooksAdapter extends RecyclerView.Adapter<HomeBooksAdapter.HomeBooksViewHolder> {

    private List<HomeBookCover> homeBookCovers ;

    public HomeBooksAdapter(List<HomeBookCover> homeBookCovers) {
        this.homeBookCovers = homeBookCovers;
    }

    @NonNull
    @Override
    public HomeBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book_cover_view, parent, false);
        return new HomeBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBooksViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(homeBookCovers.get(position).getBitmap())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return homeBookCovers.size();
    }

    public class HomeBooksViewHolder extends RecyclerView.ViewHolder{


        public ImageView imageView;

        public HomeBooksViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cover_imageview);
        }
    }

}
