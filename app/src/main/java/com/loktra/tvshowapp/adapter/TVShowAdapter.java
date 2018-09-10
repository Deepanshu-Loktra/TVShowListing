package com.loktra.tvshowapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private ArrayList<TVShowResponse> tvShowResponseArrayList;

    TVShowAdapter(ArrayList<TVShowResponse> tvShowResponseArrayList){
        this.tvShowResponseArrayList = tvShowResponseArrayList;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class TVShowViewHolder extends RecyclerView.ViewHolder{

        public TVShowViewHolder(View itemView) {
            super(itemView);
        }
    }
}
