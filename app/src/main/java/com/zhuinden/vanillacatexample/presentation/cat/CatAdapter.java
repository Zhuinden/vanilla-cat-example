package com.zhuinden.vanillacatexample.presentation.cat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhuinden.vanillacatexample.R;
import com.zhuinden.vanillacatexample.domain.object.Cat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CatAdapter
        extends RecyclerView.Adapter<CatViewHolder> {
    private List<Cat> cats;

    public CatAdapter() {
        this(Collections.emptyList());
    }

    public CatAdapter(List<Cat> cats) {
        replaceData(cats);
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_viewholder, parent, false));
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        holder.bind(cats.get(position));
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    public void replaceData(List<Cat> cats) {
        this.cats = new ArrayList<>(cats == null ? Collections.emptyList() : cats);
        notifyDataSetChanged();
    }

    public void appendData(List<Cat> cats) {
        if(cats == null) {
            return;
        }
        int size = this.cats.size();
        this.cats.addAll(cats);
        notifyItemRangeInserted(size, cats.size());
    }
}
