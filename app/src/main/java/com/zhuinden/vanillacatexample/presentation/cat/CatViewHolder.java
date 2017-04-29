package com.zhuinden.vanillacatexample.presentation.cat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhuinden.vanillacatexample.R;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.presentation.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zhuinden on 2016.07.29..
 */
public class CatViewHolder
        extends RecyclerView.ViewHolder {
    private String _sourceUrl;

    @OnClick(R.id.cat_item_container)
    public void openCat(View view) {
        if(_sourceUrl != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(_sourceUrl));
            MainActivity.get(view.getContext()).startActivity(intent);
        }
    }

    @BindView(R.id.cat_image)
    ImageView image;

    @BindView(R.id.cat_source_url)
    TextView sourceUrl;

    Context context;

    public CatViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void bind(Cat cat) {
        if(cat != null) {
            Glide.with(context).load(cat.url()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);
            sourceUrl.setText(cat.sourceUrl());
            _sourceUrl = cat.sourceUrl();
        } else {
            Glide.clear(image);
            sourceUrl.setText("");
            _sourceUrl = null;
        }
    }
}