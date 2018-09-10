package com.loktra.tvshowapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.loktra.tvshowapp.R;
import com.loktra.tvshowapp.databinding.TvShowItemsBinding;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private ArrayList<TVShowResponse> tvShowResponseArrayList;

    public TVShowAdapter() {
    }

    public void setTVShowList(ArrayList<TVShowResponse> tvShowResponseArrayList) {
        this.tvShowResponseArrayList = tvShowResponseArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TvShowItemsBinding tvShowItemsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.tv_show_items, parent, false);
        return new TVShowViewHolder(tvShowItemsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.binding.setTvShowResponse(tvShowResponseArrayList.get(position));
        Log.d("url",tvShowResponseArrayList.get(position).posterImage.original);
        holder.binding.executePendingBindings();
        Glide.with(holder.binding.getRoot().getContext())
//                .asBitmap()
//                .apply(RequestOptions.fitCenterTransform())
                .load(tvShowResponseArrayList.get(position).posterImage.original)
                .into(holder.binding.tvshowPoster);
    }

    @Override
    public int getItemCount() {
        return tvShowResponseArrayList == null ? 0 : tvShowResponseArrayList.size();
    }

    static class TVShowViewHolder extends RecyclerView.ViewHolder {

        TvShowItemsBinding binding;

        TVShowViewHolder(TvShowItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
