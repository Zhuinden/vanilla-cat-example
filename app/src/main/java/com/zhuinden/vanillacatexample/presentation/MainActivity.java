package com.zhuinden.vanillacatexample.presentation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhuinden.vanillacatexample.R;
import com.zhuinden.vanillacatexample.application.injection.ObjectGraph;
import com.zhuinden.vanillacatexample.domain.object.Cat;
import com.zhuinden.vanillacatexample.presentation.cat.CatAdapter;
import com.zhuinden.vanillacatexample.presentation.cat.CatPresenter;
import com.zhuinden.vanillacatexample.util.RecyclerViewPaginationScrollListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
        extends AppCompatActivity
        implements CatPresenter.ViewContract {
    private static final String TAG = "MainActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private CatAdapter catAdapter;

    private CatPresenter catPresenter;

    private LinearLayoutManager linearLayoutManager;
    private Parcelable linearLayoutManagerSavedState;

    private boolean didRestoreScrollState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            linearLayoutManagerSavedState = savedInstanceState.getParcelable("SCROLL_STATE");
        }
        catPresenter = ObjectGraph.get().catPresenter();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        catAdapter = new CatAdapter();
        recyclerView.setAdapter(catAdapter);
        catPresenter.attachView(this);
        recyclerView.addOnScrollListener(new RecyclerViewPaginationScrollListener(() -> {
            catPresenter.scrolledToBottom();
        }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        catPresenter.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        catPresenter.detachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SCROLL_STATE", linearLayoutManager.onSaveInstanceState());
    }

    public static MainActivity get(Context context) {
        // noinspection ResourceType
        return (MainActivity) context.getSystemService(TAG);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        if(TAG.equals(name)) {
            return this;
        }
        if(CatPresenter.TAG.equals(name)) {
            return catPresenter;
        }
        return super.getSystemService(name);
    }

    @Override
    public void appendData(List<Cat> cats) {
        catAdapter.appendData(cats);
        restoreScrollStateAfterViewRecreate(); // handle config change + process death
    }

    @Override
    public void openCat(String sourceUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(sourceUrl));
        startActivity(intent);
    }

    private void restoreScrollStateAfterViewRecreate() {
        if(!didRestoreScrollState && linearLayoutManagerSavedState != null) {
            didRestoreScrollState = true;
            linearLayoutManager.onRestoreInstanceState(linearLayoutManagerSavedState);
        }
    }
}
