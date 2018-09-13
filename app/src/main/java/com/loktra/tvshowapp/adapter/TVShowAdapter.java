package com.loktra.tvshowapp.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.loktra.tvshowapp.R;
import com.loktra.tvshowapp.databinding.TvShowItemsBinding;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;
import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder> {

    private List<TVShowResponse> tvShowResponseArrayList;
    private List<TVShowResponse> list = new ArrayList<>();

    public TVShowAdapter() {}


    //Receiving Updated TV Show List And Updating Recyclerview Data
    public void setTVShowList(List<TVShowResponse> tvShowResponseArrayList) {
        this.tvShowResponseArrayList = tvShowResponseArrayList;
        list.addAll(tvShowResponseArrayList);
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
        holder.binding.setTvShowResponse(list.get(position));
        //holder.binding.executePendingBindings();
        Glide.with(holder.binding.getRoot().getContext())
                .load(list.get(position).posterImage.medium)
                .into(holder.binding.tvshowPoster);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setsearchFilter(String searchFilter) {
        list.clear();
        Log.d("text",searchFilter);
        if(TextUtils.isEmpty(searchFilter)){
            list.addAll(tvShowResponseArrayList);
        } else {
            for (TVShowResponse item : tvShowResponseArrayList) {
                if(item.tvShowName.toLowerCase().startsWith(searchFilter.toLowerCase())){
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    static class TVShowViewHolder extends RecyclerView.ViewHolder {

        TvShowItemsBinding binding;

        TVShowViewHolder(TvShowItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
